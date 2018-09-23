package net.bdew.wurm.server.tips;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;

import java.util.List;

public class Hooks {
    public static String getCreatureTip(Creature creature, String prev) {
        return appendWithSpace(prev, " Creature tip for #" + creature.getWurmId());
    }

    public static String getItemTip(Item item, String prev) {
        StringBuilder tip = new StringBuilder(prev);
        if (prev.length() > 0 && !prev.endsWith(" "))
            tip.append(" ");

        List<String> tips = EnchantUtils.getEnchants(item);

        if (tips.size() > 0)
            tip.append(String.join(", ", tips));

        return tip.toString();
    }

    private static String appendWithSpace(String a, String b) {
        if (a.length() > 0 && !a.endsWith(" "))
            return a + " " + b;
        else
            return b;
    }
}
