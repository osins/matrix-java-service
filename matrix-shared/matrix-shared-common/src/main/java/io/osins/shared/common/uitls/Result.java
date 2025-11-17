package io.osins.shared.common.uitls;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 通用返回结果封装类
 *
 * @param <T> 返回数据类型
 */
@Data
@Slf4j
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String message;

    @JsonIgnoreProperties(value = "retry_after_ms")
    private Integer retryAfterMs;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 状态码常量
     */
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;

    /**
     * 私有构造函数
     *
     * @param success 是否成功
     * @param code    状态码
     * @param message 返回消息
     * @param data    返回数据
     */
    private Result(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private Result(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }


    /**
     * 成功时返回数据
     *
     * @param data 返回的数据
     * @param <T>  数据类型
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(true, CODE_SUCCESS, "操作成功", data);
    }

    public static Result<Void> success() {
        return new Result<>(true, CODE_SUCCESS, "操作成功");
    }

    /**
     * 成功时返回数据
     *
     * @param message 成功消息
     * @param data    返回的数据
     * @param <T>     数据类型
     * @return Result<T>
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(true, CODE_SUCCESS, message, data);
    }

    /**
     * 失败时返回错误信息
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return Result<T>
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(false, code, message, null);
    }

    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(false, code, message, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(false, CODE_ERROR, message, null);
    }


    /**
     * 失败时返回错误信息
     *
     * @param message 错误消息
     * @param <T>     数据类型
     * @return Result<T>
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(false, CODE_ERROR, message, null);
    }

    public static <T> Result<T> of(boolean returnValue, String successMessage, String failMessage) {
        if (returnValue) {
            return Result.success(successMessage, null);
        } else {
            return Result.failure(failMessage);
        }
    }

    public static <T> Result<T> of(boolean returnValue, T data, String successMessage, String failMessage) {
        if (returnValue) {
            return Result.success(successMessage, data);
        } else {
            return Result.failure(failMessage);
        }
    }

    /**
     * 转换结果数据类型
     *
     * @param mapper 转换函数
     * @param <R>    新的数据类型
     * @return 转换后的结果
     */
    public <R> Result<R> map(Function<T, R> mapper) {
        if (!success) {
            return error(code, message);
        }
        try {
            R newData = mapper.apply(data);
            return new Result<R>(true, code, message, newData);
        } catch (Exception e) {
            return error(500, "数据转换失败: " + e.getMessage());
        }
    }

    /**
     * flatMap方法，用于处理嵌套的Result对象转换
     *
     * @param mapper 转换函数，返回新的Result对象
     * @param <R>    新的数据类型
     * @return 转换后的结果
     */
    public <R> R flatMap(Function<T, R> mapper) {
        if (!success) {
            return null;
        }
        try {
            return mapper.apply(data);
        } catch (Exception e) {
            return null;
        }
    }

    public Result<T> ifSuccess(Runnable runnable) {
        if (success) {
            runnable.run();
        }

        return this;
    }

    public Result<T> ifSuccess(Consumer<T> runnable) {
        if (success && data != null) {
            runnable.accept(data);
        }

        return this;
    }

    public Result<T> ifFail(Runnable runnable) {
        if (!success) {
            runnable.run();
        }

        return this;
    }

    public Result<T> andRun(Runnable runnable){
        try {
            runnable.run();
        } catch (Exception e) {
            log.debug("result and run error: {}, {}", e.getMessage(), this, e);
        }

        return this;
    }

    public Result<T> andConsume(Consumer<T> consumer){
        try {
            consumer.accept(data);
        } catch (Exception e) {
            log.debug("result and run error: {}, {}", e.getMessage(), this, e);
        }

        return this;
    }

    public Result<T> andConsumeAsync(Consumer<T> consumer){
        try {
            CompletableFuture.runAsync(()->{
                consumer.accept(data);
            }).thenAccept(v->{
                log.debug("result consume completed: {}", this);
            });
        } catch (Exception e) {
            log.debug("result and run error: {}, {}", e.getMessage(), this, e);
        }

        return this;
    }

    public void orElse(Runnable runnable) {
        if (success) {
            return;
        }

        runnable.run();
    }

    public Result<T> orElse(Supplier<T> supplier) {
        if (success) {
            return this;
        }

        return this.setData(supplier.get());
    }

    public <R> Result<R> convert(Function<T, R> mapper) {
        if(success)
            return Result.success(message, mapper.apply(data));

        return Result.failure(code, message);
    }
}