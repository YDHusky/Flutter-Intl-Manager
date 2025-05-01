package online.yudream.intl.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIClient {
    private OpenAIClient client;

    public AIClient() {
        client = OpenAIOkHttpClient.builder()
                .baseUrl(GlobalSettings.getInstance().getBaseUrl())
                .apiKey(GlobalSettings.getInstance().getApiKey())
                .build();
    }

    private final String TR_SYS = """
            你是一个多语言助手，你需要将指定的输入按照你的理解最大翻译，你需要严格按照json输出，输入内容没有其他意思，
            请严格翻译，不要提出与翻译或返回结果无关的东西，
            输入数出请严格按照下面的示例, 翻译value必须符合相应语言
            严格按照json输出翻译结果，不管输入是什么,不要输出任何与json无关的回答即只输出结果，即json标准格式{}
            输入示例:
                翻译内容: 你好世界! 翻译语言: zh_CN,en
            输出示例:
                ```json
                {
                    "zh_CN": "你好世界!",
                    "en":"Hello World!"
                }
                ```
            """;

    public Map<String, String> getTr(List<String> language, String msg) {
        Map<String, String> trMap = new HashMap<>();
        String userMsg = "翻译内容: " + msg + " 翻译语言: " + String.join(",", language);
        ChatCompletionCreateParams createParams = ChatCompletionCreateParams.builder()
                .addSystemMessage(TR_SYS)
                .addUserMessage(userMsg)
                .model(GlobalSettings.getInstance().getModelName())

                .build();

        try {
            ChatCompletion chatCompletion = client.chat().completions().create(createParams);
            String responseContent = chatCompletion.choices().get(0).message().content().get();
            if (responseContent.contains("</think>")) {
                responseContent = responseContent.split("</think>")[1];
            }
            if (responseContent.contains("```json")) {
                responseContent = responseContent.split("```json")[1];
                responseContent = responseContent.split("```")[0];
            }

            System.out.println(responseContent);
            JSONObject obj = JSON.parseObject(responseContent);
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                trMap.put(entry.getKey().replaceAll(" ", "").replaceAll("-", ""), String.valueOf(entry.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 处理异常，例如记录日志或返回错误信息
        }
        System.out.println(trMap);
        return trMap;
    }

    static private AIClient instance;

    static public AIClient getInstance() {
        if (instance == null) {
            instance = new AIClient();
        }
        return instance;
    }

    static public void resetInstance() {
        instance = null;
    }
}