package io.osins.matrix.auth.oauth2.server.enums;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Optional;

@Getter
public enum ResponseContentType {
    NONE("none", "无类型"),
    STRING("string", "字符类型"),
    XML("xml", "xml类型"),
    JSON("json", "json类型"),
    HTML("html", "html类型"),
    REDIRECT_PARAMS("redirect_params", "url跳转链接和地址参数");

    private final String type;
    private final String description;

    ResponseContentType(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public boolean isNone() {
        return NONE.equals(this);
    }

    public static String getDescription(String type) {
        for (ResponseContentType responseContentType : values()) {
            if (responseContentType.getType().equals(type)) {
                return responseContentType.getDescription();
            }
        }
        return NONE.getDescription();
    }

    public static ResponseContentType of(String type) {
        if (type == null || Strings.isNullOrEmpty(type)) {
            return NONE;
        }

        for (ResponseContentType responseContentType : values()) {
            if (responseContentType.getType().equalsIgnoreCase(type)) {
                return responseContentType;
            }
        }

        return NONE;
    }

    public boolean isString() {
        return STRING.equals(this);
    }

    public boolean isXml() {
        return XML.equals(this);
    }

    public boolean isJson() {
        return JSON.equals(this);
    }

    public boolean isHtml() {
        return HTML.equals(this);
    }

    public boolean isRedirectParams() {
        return REDIRECT_PARAMS.equals(this);
    }

    /**
     * 支持的类型
     * @return boolean
     */
    public boolean supported() {
        return isJson() || isRedirectParams();
    }

    /**
     * 不支持的类型
     */
    public boolean unsupported() {
        return !supported();
    }

    public Optional<ResponseTypeHandler> handler(EnumMap<ResponseContentType, ResponseTypeHandler> handlers) {
        return Optional.ofNullable(handlers.get(this));
    }

    public <R> Optional<ResponseTypeMethodHandler<R>> method(EnumMap<ResponseContentType, ResponseTypeMethodHandler<R>> handlers) {
        return Optional.ofNullable(handlers.get(this));
    }

    @FunctionalInterface
    public interface ResponseTypeMethod<R>{
        R get(ResponseContentType t);
    }

    @Data
    @AllArgsConstructor
    public static class ResponseTypeMethodHandler<R>{
        private ResponseContentType type;
        private ResponseTypeMethod<R> method;

        public static <R> ResponseTypeMethodHandler<R> builder(ResponseContentType type, ResponseTypeMethod<R> method){
            return new ResponseTypeMethodHandler<>(type, method);
        }

        public R method(){
            return method.get(type);
        }
    }

    @FunctionalInterface
    public interface ResponseTypeConsumer {
        void apply(ResponseContentType t);
    }

    @Data
    @AllArgsConstructor
    public static class ResponseTypeHandler{
        private ResponseContentType type;
        private ResponseTypeConsumer consumer;

        public static ResponseTypeHandler builder(ResponseContentType type, ResponseTypeConsumer consumer){
            return new ResponseTypeHandler(type, consumer);
        }

        public void consume(){
            consumer.apply(type);
        }
    }
}
