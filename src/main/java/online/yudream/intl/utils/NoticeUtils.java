package online.yudream.intl.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class NoticeUtils {
    public static void showNotification(String message, NotificationType type) {
        Notification notification = new Notification("FlutterIntlManager", "操作结果", message, type);
        Notifications.Bus.notify(notification);
    }
}
