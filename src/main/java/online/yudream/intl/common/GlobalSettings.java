package online.yudream.intl.common;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "online.yudream.intl.common.GlobalSettings",
        storages = {@Storage(value = "YudreamIntlGlobalSettings.xml")}
)
public class GlobalSettings implements PersistentStateComponent<GlobalSettings> {

    public String baseUrl = "http://127.0.0.1:11434/v1";
    public String apiKey = "qwen:7b";
    public String defaultLanguage = "zh_CN";
    public String modelName = "";

    @Override
    public @Nullable GlobalSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalSettings globalSettings) {
        this.modelName = globalSettings.modelName;
        this.baseUrl = globalSettings.baseUrl;
        this.apiKey = globalSettings.apiKey;
        this.defaultLanguage = globalSettings.defaultLanguage;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


    public static GlobalSettings getInstance() {
        return ApplicationManager.getApplication().getService(GlobalSettings.class).getState();
    }
}