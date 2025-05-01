package online.yudream.intl.entity;


public class Config {
    private String l10nFolderPath = "lib/l10n";
    private String language = "en";

    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getL10nFolderPath() {
        return l10nFolderPath;
    }

    public void setL10nFolderPath(String l10nFolderPath) {
        this.l10nFolderPath = l10nFolderPath;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public static void setInstance(Config instance) {
        Config.instance = instance;
    }
}
