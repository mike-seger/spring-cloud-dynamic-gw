package com.net128.app.dynrouting;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceUtil {
    public static String loadResourceFromLocation(String location) {
        var resource = ResourceUtil.class.getClassLoader().getResource(location);
        if(resource==null )  throw new RuntimeException("Unable to find resource at: "+location);
        try { return Files.readString(Paths.get(Objects.requireNonNull(resource).toURI())); }
        catch (Exception e) { throw new RuntimeException("Exception trying to access resource at: "+location, e); }
    }
}