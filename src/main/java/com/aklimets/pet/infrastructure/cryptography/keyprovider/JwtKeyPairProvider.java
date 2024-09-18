package com.aklimets.pet.infrastructure.cryptography.keyprovider;

import com.aklimets.pet.crypto.model.AsymmetricAlgorithm;
import com.aklimets.pet.crypto.provider.VersionedKeyPairProvider;
import com.aklimets.pet.crypto.util.AsymmetricKeyUtil;
import com.aklimets.pet.domain.dto.key.PublicKeyResponse;
import com.aklimets.pet.domain.dto.key.SecretsResponseWrapper;
import com.aklimets.pet.domain.dto.key.StoredKeyResponse;
import com.aklimets.pet.domain.dto.key.StoredKeyVersionResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtKeyPairProvider implements VersionedKeyPairProvider {

    private final String keyName;

    private final RestTemplate restTemplate;

    private final String secretsBaseUri;

    private final AsymmetricKeyUtil asymmetricKeyUtil;

    private PublicKey publicKey;

    private Map<String, PublicKey> publicKeyVersions = new HashMap<>();

    public JwtKeyPairProvider(String keyName, String secretsBaseUri, RestTemplate restTemplate, AsymmetricKeyUtil asymmetricKeyUtil) {
        this.keyName = keyName;
        this.secretsBaseUri = secretsBaseUri;
        this.restTemplate = restTemplate;
        this.asymmetricKeyUtil = asymmetricKeyUtil;
    }

    @Override
    @SneakyThrows
    public PublicKey getPublicKey() {
        if (publicKey == null) {
            populateKeys();
        }
        return publicKey;
    }

    @Override
    @SneakyThrows
    public PrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public PublicKey getPublicKey(String version) {
        if (publicKeyVersions.isEmpty() || !publicKeyVersions.containsKey(version)) {
            populateKeys();
        }
        return publicKeyVersions.get(version);
    }

    @Override
    public PrivateKey getPrivateKey(String version) {
        return null;
    }

    @SneakyThrows
    private void populateKeys() {
        var storedKey = getStoredKey();
        storedKey.versions().forEach(this::getKeyForVersion);
    }

    @SneakyThrows
    private void getKeyForVersion(StoredKeyVersionResponse version) {
        var currentVersion = version.id();
        var publicKeyResponse = getPublicKeyResponse(currentVersion);
        var localPublicKey = asymmetricKeyUtil.getPublicKeyInstance(publicKeyResponse.getPublicKey(), AsymmetricAlgorithm.valueOf(publicKeyResponse.getAlgorithm()));

        publicKeyVersions.put(currentVersion, localPublicKey);
        if (version.state().equals("CURRENT")) {
            publicKey = localPublicKey;
        }
    }

    private StoredKeyResponse getStoredKey() {
        return restTemplate.exchange(
                secretsBaseUri + "/api/v1/keys/" + keyName,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SecretsResponseWrapper<StoredKeyResponse>>() {
                }).getBody().getData();
    }

    private PublicKeyResponse getPublicKeyResponse(String currentVersion) {
        return restTemplate.exchange(
                secretsBaseUri + "/api/v1/keys/" + keyName + "/public/" + currentVersion + "?includeDisposableKey=false",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<SecretsResponseWrapper<PublicKeyResponse>>() {
                }).getBody().getData();
    }
}
