

package main.java.dev.sprint;

import java.util.Objects;

import main.java.dev.sprint.annotation.Url;
import main.java.dev.sprint.constant.HttpMethod;

public class UrlInfo {

    private HttpMethod method;
    private String url;

    public UrlInfo(Url url) {
        this.method = url.method();
        this.url = url.value();
    }

    public UrlInfo(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, url);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UrlInfo urlInfo) {
            return urlInfo.getMethod().equals(this.method) && urlInfo.getUrl().equals(this.url);
        }
        return false;
    }
}