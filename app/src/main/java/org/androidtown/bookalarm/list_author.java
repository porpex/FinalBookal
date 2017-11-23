package org.androidtown.bookalarm;

/**
 * Created by wlals on 2017-09-26.
 */
public class list_author {
    private  String userAuthor;
    private  String userDate;
    private  String userImageUrl;
    private  String userUrl;
    private  String userBookName;
    private  String userPubDate;
    private  String userKeyBook;
    public list_author( String userAuthor, String userDate, String userImageUrl, String userUrl, String userBookName) {
        this.userAuthor = userAuthor;
        this.userDate = userDate;
        this.userImageUrl = userImageUrl;
        this.userUrl = userUrl;
        this.userBookName = userBookName;
    }

    public list_author( String userAuthor, String userImageUrl, String userUrl, String userBookName, String userPubDate,String userKeyBook) {
        this.userAuthor = userAuthor;
        this.userImageUrl = userImageUrl;
        this.userUrl = userUrl;
        this.userBookName = userBookName;
        this.userPubDate = userPubDate;
        this.userKeyBook = userKeyBook;
    }

    public String getUserAuthor() {
        return userAuthor;
    }

    public void setUserAuthor(String userAuthor) {
        this.userAuthor = userAuthor;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getUserImagerUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImagerUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserBookName() {
        return userBookName;
    }

    public void setUserBookName(String userBookName) {
        this.userBookName = userBookName;
    }

    public String getUserPubDate() {
        return userPubDate;
    }

    public void setUserPubDate(String userPubDate) {
        this.userPubDate = userPubDate;
    }
}

