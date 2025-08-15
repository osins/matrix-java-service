package club.hm.homemart.club.shared.common.domain;

import lombok.*;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReX {
    private String text;
    private String result;

    @Builder.Default
    private LinkedHashMap<String, String> rules = new LinkedHashMap<>();

    public ReX addRule(String pattern, String template){
        rules.put(pattern, template);
        return this;
    }

    public ReX replace(){
        result = text;
        rules.forEach((p, t)->{
            result = replace(result, p, t);
        });

        return this;
    }

    private String replace(String text, String pattern, String template) {
        var matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(text);
        if (!matcher.find()) {
            return text;
        }

        var sb = new StringBuffer();

        do {
            matcher.appendReplacement(sb, Matcher.quoteReplacement(resolve(template, matcher)));
        } while (matcher.find());

        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String resolve(String template, Matcher matcher) {
        var result = template;
        for (int i = 1; i <= matcher.groupCount(); i++) {
            var g = matcher.group(i);
            if (g == null) continue;
            result = result.replace("$" + i, g);
        }
        return result;
    }
}
