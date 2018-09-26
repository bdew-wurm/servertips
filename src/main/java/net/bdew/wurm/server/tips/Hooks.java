package net.bdew.wurm.server.tips;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.WurmId;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.shared.constants.CounterTypes;

import java.util.List;

public class Hooks {
    public static String getCreatureTip(Creature creature, String prev) {
        return prev;
    }

    public static String getItemTip(Item item, String prev) {
        StringBuilder tip = new StringBuilder(prev);
        if (prev.length() > 0 && !prev.endsWith(" "))
            tip.append("\n");

        List<String> tips = EnchantUtils.getEnchants(item);

        if (tips.size() > 0)
            tip.append(String.join(", ", tips));

        if (item.getParentId() == -10 && item.getZoneId() > 0) {
            if (tip.length() > 0) tip.append("\n");

            tip.append(String.format("QL:%.2f", item.getCurrentQualityLevel()));

            if (item.getDamage() > 0)
                tip.append(String.format(" Dmg:%.2f", item.getDamage()));

            String improve = ItemUtils.getImproveString(item);
            if (improve != null) tip.append(" Imp:").append(improve);
        }

        return tip.toString();
    }

    public static void spellEffectChanged(SpellEffect eff) {
        if (WurmId.getType(eff.owner) == CounterTypes.COUNTER_TYPE_ITEMS) {
            try {
                itemChanged(Items.getItem(eff.owner));
            } catch (NoSuchItemException e) {
                ServerTipsMod.logException("Item not found in spellEffectChanged", e);
            }
        }
    }

    public static void itemChanged(Item item) {
        ItemUtils.refreshItem(item);
    }
}
