package pl.alekaug.quizplatform;

import jakarta.servlet.http.Cookie;

public abstract class CookieHelper {
    public static Cookie getCookie(Cookie[] cookies, String cookieName) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }

    public static Cookie emptyCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private CookieHelper() { /* NoOp */ }

    public static Cookie registerCookie(String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(604_800); // 7 days in seconds
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
