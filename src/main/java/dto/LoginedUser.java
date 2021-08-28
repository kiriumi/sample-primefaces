package dto;

import java.time.LocalDateTime;

/**
 * Database Table Remarks:
 *   ログインユーザ
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table public.logined_user
 */
public class LoginedUser {
    /**
     * Database Column Remarks:
     *   セッションID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.logined_user.session_id
     *
     * @mbg.generated
     */
    private String sessionId;

    /**
     * Database Column Remarks:
     *   ID
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.logined_user.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     * Database Column Remarks:
     *   作成日時
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.logined_user.createdtime
     *
     * @mbg.generated
     */
    private LocalDateTime createdtime;

    /**
     * Database Column Remarks:
     *   更新日時
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column public.logined_user.updatedtime
     *
     * @mbg.generated
     */
    private LocalDateTime updatedtime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.logined_user.session_id
     *
     * @return the value of public.logined_user.session_id
     *
     * @mbg.generated
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.logined_user.session_id
     *
     * @param sessionId the value for public.logined_user.session_id
     *
     * @mbg.generated
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.logined_user.id
     *
     * @return the value of public.logined_user.id
     *
     * @mbg.generated
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.logined_user.id
     *
     * @param id the value for public.logined_user.id
     *
     * @mbg.generated
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.logined_user.createdtime
     *
     * @return the value of public.logined_user.createdtime
     *
     * @mbg.generated
     */
    public LocalDateTime getCreatedtime() {
        return createdtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.logined_user.createdtime
     *
     * @param createdtime the value for public.logined_user.createdtime
     *
     * @mbg.generated
     */
    public void setCreatedtime(LocalDateTime createdtime) {
        this.createdtime = createdtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column public.logined_user.updatedtime
     *
     * @return the value of public.logined_user.updatedtime
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdatedtime() {
        return updatedtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column public.logined_user.updatedtime
     *
     * @param updatedtime the value for public.logined_user.updatedtime
     *
     * @mbg.generated
     */
    public void setUpdatedtime(LocalDateTime updatedtime) {
        this.updatedtime = updatedtime;
    }
}