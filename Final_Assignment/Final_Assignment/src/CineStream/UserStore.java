package CineStream;

import java.util.HashMap;
import java.util.Map;

/**
 * 临时用户存储：仅运行时有效，关闭程序记录自动清空
 */
public class UserStore {
    // 单例，全局唯一实例
    private static UserStore instance;
    // 保存邮箱和密码
    private final Map<String, String> registeredUsers = new HashMap<>();

    // 私有构造，防止外部实例化
    private UserStore() {}

    // 获取实例
    public static UserStore getInstance() {
        if (instance == null) {
            instance = new UserStore();
        }
        return instance;
    }

    // 注册：保存邮箱和密码
    public void register(String email, String password) {
        if (email != null && password != null) {
            registeredUsers.put(email.trim().toLowerCase(), password);
        }
    }

    // 检查：邮箱是否已注册
    public boolean isRegistered(String email) {
        if (email == null) return false;
        return registeredUsers.containsKey(email.trim().toLowerCase());
    }

    // 验证：邮箱和密码是否匹配
    public boolean validateLogin(String email, String password) {
        if (email == null || password == null) return false;
        String storedPassword = registeredUsers.get(email.trim().toLowerCase());
        return storedPassword != null && storedPassword.equals(password);
    }

    // 可选：清空所有记录（程序关闭自动失效，也可手动调用）
    public void clearAll() {
        registeredUsers.clear();
    }
}
