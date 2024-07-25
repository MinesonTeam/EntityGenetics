package kz.hxncus.mc.entitygenetics.color;

import com.google.common.collect.ImmutableMap;
import kz.hxncus.mc.entitygenetics.EntityGenetics;
import kz.hxncus.mc.entitygenetics.color.caching.LruCache;
import kz.hxncus.mc.entitygenetics.color.pattern.GradientPattern;
import kz.hxncus.mc.entitygenetics.color.pattern.Pattern;
import kz.hxncus.mc.entitygenetics.color.pattern.RainbowPattern;
import kz.hxncus.mc.entitygenetics.color.pattern.SolidPattern;
import kz.hxncus.mc.entitygenetics.util.ReflectionUtil;
import kz.hxncus.mc.entitygenetics.util.VersionUtil;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class ColorManager {
    private final List<String> SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m");
    private final LruCache LRU_CACHE = new LruCache(1);
    private final ImmutableMap<Color, ChatColor> COLORS = new ImmutableMap.Builder<Color, ChatColor>()
            .put(new Color(0), ChatColor.getByChar('0'))
            .put(new Color(170), ChatColor.getByChar('1'))
            .put(new Color(43520), ChatColor.getByChar('2'))
            .put(new Color(43690), ChatColor.getByChar('3'))
            .put(new Color(11141120), ChatColor.getByChar('4'))
            .put(new Color(11141290), ChatColor.getByChar('5'))
            .put(new Color(16755200), ChatColor.getByChar('6'))
            .put(new Color(11184810), ChatColor.getByChar('7'))
            .put(new Color(5592405), ChatColor.getByChar('8'))
            .put(new Color(5592575), ChatColor.getByChar('9'))
            .put(new Color(5635925), ChatColor.getByChar('a'))
            .put(new Color(5636095), ChatColor.getByChar('b'))
            .put(new Color(16733525), ChatColor.getByChar('c'))
            .put(new Color(16733695), ChatColor.getByChar('d'))
            .put(new Color(16777045), ChatColor.getByChar('e'))
            .put(new Color(16777215), ChatColor.getByChar('f'))
            .build();
    private static EntityGenetics plugin;
    private final List<Pattern> PATTERNS = Arrays.asList(new GradientPattern(plugin), new SolidPattern(plugin), new RainbowPattern(plugin));

    public ColorManager(EntityGenetics plugin) {
        ColorManager.plugin = plugin;
    }

    @NonNull
    public String process(@NonNull String string) {
        String result = LRU_CACHE.getResult(string);
        if (result == null) {
            String input = string;
            for (Pattern pattern : PATTERNS) {
                string = pattern.process(string);
            }
            string = ChatColor.translateAlternateColorCodes('&', string);
            LRU_CACHE.put(input, string);
            return string;
        }
        return result;
    }

    @NonNull
    public List<String> process(@NonNull List<String> strings) {
        strings.replaceAll(this::process);
        return strings;
    }

    @NonNull
    public String color(@NonNull String string, @NonNull Color color) throws InvocationTargetException, IllegalAccessException {
        return (VersionUtil.IS_HEX_VERSION ? (String) ReflectionUtil.getMethod(ChatColor.class, "of", Color.class).invoke(color) : getClosestColor(color)) + string;
    }

    @NonNull
    public String color(@NonNull String string, @NonNull Color start, @NonNull Color end) {
        StringBuilder specialColors = new StringBuilder();
        for (String color : SPECIAL_COLORS) {
            if (string.contains(color)) {
                specialColors.append(color);
                string = string.replace(color, "");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        ChatColor[] colors = createGradient(start, end, string.length());
        String[] characters = string.split("");
        for (int i = 0; i < string.length(); i++)
            stringBuilder.append(colors[i])
                         .append(specialColors)
                         .append(characters[i]);
        return stringBuilder.toString();
    }

    @NonNull
    public String rainbow(@NonNull String string, float saturation) {
        StringBuilder specialColors = new StringBuilder();
        for (String color : SPECIAL_COLORS) {
            if (string.contains(color)) {
                specialColors.append(color);
                string = string.replace(color, "");
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        ChatColor[] colors = createRainbow(string.length(), saturation);
        String[] characters = string.split("");
        for (int i = 0; i < string.length(); i++)
            stringBuilder.append(colors[i])
                         .append(specialColors)
                         .append(characters[i]);
        return stringBuilder.toString();
    }

    @NonNull
    public ChatColor getColor(@NonNull String string) {
        try {
            return VersionUtil.IS_HEX_VERSION ? (ChatColor) ReflectionUtil.getMethod(ChatColor.class, "of", Color.class)
                                                                          .invoke(new Color(Integer.parseInt(string, 16))) : getClosestColor(new Color(Integer.parseInt(string, 16)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    public String stripColorFormatting(@NonNull String string) {
        return string.replaceAll("&\\w{5,8}(:[0-9A-F]{6})?>", "");
    }

    @NonNull
    private ChatColor[] createRainbow(int step, float saturation) {
        ChatColor[] colors = new ChatColor[step];
        double colorStep = 1.0D / step;
        for (int i = 0; i < step; i++) {
            Color color = Color.getHSBColor((float) (colorStep * i), saturation, saturation);
            if (VersionUtil.IS_HEX_VERSION) {
                try {
                    colors[i] = (ChatColor) ReflectionUtil.getMethod(ChatColor.class, "of", Color.class)
                                                          .invoke(color);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                colors[i] = getClosestColor(color);
            }
        }
        return colors;
    }

    @NonNull
    private ChatColor[] createGradient(@NonNull Color start, @NonNull Color end, int step) {
        if (step <= 1)
            return new ChatColor[]{ChatColor.WHITE, ChatColor.WHITE, ChatColor.WHITE};
        ChatColor[] colors = new ChatColor[step];
        int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
        int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
        int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);
        int[] direction = {(start.getRed() < end.getRed()) ? 1 : -1, (start.getGreen() < end.getGreen()) ? 1 : -1, (start.getBlue() < end.getBlue()) ? 1 : -1};
        for (int i = 0; i < step; i++) {
            Color color = new Color(start.getRed() + stepR * i * direction[0], start.getGreen() + stepG * i * direction[1], start.getBlue() + stepB * i * direction[2]);
            if (VersionUtil.IS_HEX_VERSION) {
                try {
                    colors[i] = (ChatColor) ReflectionUtil.getMethod(ChatColor.class, "of", Color.class)
                                                          .invoke(color);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else {
                colors[i] = getClosestColor(color);
            }
        }
        return colors;
    }

    private ChatColor getClosestColor(@NonNull Color color) {
        Color nearestColor = null;
        double nearestDistance = 2.147483647E9D;
        for (Color colors : COLORS.keySet()) {
            double distance = Math.pow((color.getRed() - colors.getRed()), 2.0D) + Math.pow((color.getGreen() - colors.getGreen()), 2.0D) + Math.pow((color.getBlue() - colors.getBlue()), 2.0D);
            if (nearestDistance > distance) {
                nearestColor = colors;
                nearestDistance = distance;
            }
        }
        return COLORS.get(nearestColor);
    }
}
