package cn.iocoder.forum.common;

public class AppResult<T> {
    private int code;
    private String message;
    private T data;

    /**
     * 构造方法
     * @param code
     * @param message
     * @param data
     */
    public AppResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public AppResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 成功
     * @return
     */
    public static AppResult success() {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    // data返回的结果
    public static <T> AppResult<T> success(T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    // 自定义描述+data返回的结果
    public static <T> AppResult<T> success(int code, String message, T data) {
        return new AppResult(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败
     * @return
     */
    public static AppResult failed() {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }
    // 自定义描述
    public static <T> AppResult<T> failed(String message) {
        return new AppResult(ResultCode.FAILED.getCode(), message);
    }
    // 异常信息
    public static <T> AppResult<T> failed(T  data) {
        return new AppResult(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), data);
    }
    // 错误状态
    public static <T> AppResult<T> failed(ResultCode resultCode) {
        return new AppResult(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * getter setter
     * @return
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
