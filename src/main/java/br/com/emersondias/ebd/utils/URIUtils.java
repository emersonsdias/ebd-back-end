package br.com.emersondias.ebd.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class URIUtils {

    public static URI buildUri(UUID id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id]").buildAndExpand(id).toUri();
    }

    public static URI buildUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id]").buildAndExpand(id).toUri();
    }


    public static UUID findUuidAfterPath(String url, String path) {
        var id = findNextSegmentAfterPath(url, path);
        if (nonNull(id)) {
            return UUID.fromString(id);
        }
        return null;
    }

    public static String findNextSegmentAfterPath(String url, String path) {
        requireNonNull(url);
        requireNonNull(path);
        if (!url.startsWith("/")) {
            url = "/".concat(url);
        }
        if (!path.startsWith("/")) {
            path = "/".concat(path);
        }
        if (!path.endsWith("/")) {
            path = path.concat("/");
        }
        if (!url.contains(path)) {
            return null;
        }
        int index = url.indexOf(path);
        String subPath = url.substring(index + path.length());
        return subPath.split("/")[0];
    }

}
