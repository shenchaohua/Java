package cep;

public class UserAction {
    private Long userId;
    private String userAction;
    private Long ts;

    public UserAction(Long userId, String userAction, Long ts) {
        this.userId = userId;
        this.userAction = userAction;
        this.ts = ts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "UserAction{" +
                "userId=" + userId +
                ", userAction='" + userAction + '\'' +
                ", ts=" + ts +
                '}';
    }
}
