package net.bdew.wurm.server.tips;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemSpellEffects;
import com.wurmonline.server.items.RuneUtilities;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.spells.Spells;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.*;

public class EnchantUtils {
    private static Map<Byte, List<String>> runeStrings;

    public static List<String> getEnchants(Item item) {
        LinkedList<String> tips = new LinkedList<>();
        ItemSpellEffects effects = item.getSpellEffects();
        if (effects != null) {
            for (SpellEffect effect : effects.getEffects()) {
                if (effect.type < 0) {
                    if (runeStrings.containsKey(effect.type))
                        tips.addAll(runeStrings.get(effect.type));
                } else if (effect.isSmeared()) {
                    switch (effect.type) {
                        case 78:
                            tips.add("RM+ " + (int) effect.power);
                            break;
                        case 79:
                            tips.add("Mine+ " + (int) effect.power);
                            break;
                        case 77:
                            tips.add("WS+ " + (int) effect.power);
                            break;
                        case 80:
                            tips.add("Tailor+ " + (int) effect.power);
                            break;
                        case 81:
                            tips.add("AS+ " + (int) effect.power);
                            break;
                        case 82:
                            tips.add("Fletch+ " + (int) effect.power);
                            break;
                        case 83:
                            tips.add("BS+ " + (int) effect.power);
                            break;
                        case 84:
                            tips.add("LW+ " + (int) effect.power);
                            break;
                        case 85:
                            tips.add("SHB+ " + (int) effect.power);
                            break;
                        case 86:
                            tips.add("SC+ " + (int) effect.power);
                            break;
                        case 87:
                            tips.add("Mason+ " + (int) effect.power);
                            break;
                        case 88:
                            tips.add("WC+ " + (int) effect.power);
                            break;
                        case 89:
                            tips.add("Carp+ " + (int) effect.power);
                            break;
                        case 99:
                            tips.add("Butcher+ " + (int) effect.power);
                            break;
                    }
                } else if (effect.type == 20) {
                    if (item.isMailBox())
                        tips.add("Courier " + (int) effect.power);
                    else
                        tips.add("BoH " + (int) effect.power);
                } else {
                    if (effect.power > 0)
                        tips.add(shortName(effect.getName()) + " " + (int) effect.power);
                    else
                        tips.add(shortName(effect.getName()));
                }
            }
        }

        if (item.enchantment != 0) {
            Spell ench = Spells.getEnchantment(item.enchantment);
            if (ench != null)
                tips.add(shortName(ench.getName()));
            else if (item.enchantment == 90)
                tips.add("Acid");
            else if (item.enchantment == 91)
                tips.add("Fire");
            else if (item.enchantment == 92)
                tips.add("Frost");
        }

        return tips;
    }

    private static String runeFormat(String eff, float power) {
        if (power > 0)
            return String.format("%s +%.0f%%", eff, power * 100f);
        else if (power < 0) {
            return String.format("%s %.0f%%", eff, power * 100f);
        } else return eff;
    }

    public static void prepare() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        runeStrings = new HashMap<>();
        Field fModifierMap = ReflectionUtil.getField(Class.forName("com.wurmonline.server.items.RuneUtilities$RuneData"), "modifierMap");
        Field fIsEnchantment = ReflectionUtil.getField(Class.forName("com.wurmonline.server.items.RuneUtilities$RuneData"), "isEnchantment");
        Map<Byte, Object> runeDataMap = ReflectionUtil.getPrivateField(null, ReflectionUtil.getField(RuneUtilities.class, "runeDataMap"));
        for (Map.Entry<Byte, Object> rune : runeDataMap.entrySet()) {
            if (ReflectionUtil.getPrivateField(rune.getValue(), fIsEnchantment)) {
                List<String> strings = new ArrayList<>();

                Map<RuneUtilities.ModifierEffect, Float> modifierMap = ReflectionUtil.getPrivateField(rune.getValue(), fModifierMap);

                for (Map.Entry<RuneUtilities.ModifierEffect, Float> entry : modifierMap.entrySet()) {
                    switch (entry.getKey()) {
                        case ENCH_WEIGHT:
                            strings.add(runeFormat("Weight", entry.getValue()));
                            break;
                        case ENCH_VOLUME:
                            strings.add(runeFormat("Volume", entry.getValue()));
                            break;
                        case ENCH_DAMAGETAKEN:
                            strings.add(runeFormat("DmgTaken", entry.getValue()));
                            break;
                        case ENCH_USESPEED:
                            strings.add(runeFormat("Speed", entry.getValue()));
                            break;
                        case ENCH_SIZE:
                            strings.add(runeFormat("Size", entry.getValue()));
                            break;
                        case ENCH_SKILLCHECKBONUS:
                            strings.add(runeFormat("Skill", entry.getValue()));
                            break;
                        case ENCH_SHATTERRES:
                            strings.add(runeFormat("Shatter Res", entry.getValue()));
                            break;
                        case ENCH_DECAY:
                            strings.add(runeFormat("Decay", entry.getValue()));
                            break;
                        case ENCH_INTERNAL_DECAY:
                            strings.add(runeFormat("Content Decay", entry.getValue()));
                            break;
                        case ENCH_VEHCSPEED:
                            strings.add(runeFormat("Vehicle Speed", entry.getValue()));
                            break;
                        case ENCH_WIND:
                            strings.add(runeFormat("Wind Speed", entry.getValue()));
                            break;
                        case ENCH_IMPQL:
                            strings.add(runeFormat("Improve QL", entry.getValue()));
                            break;
                        case ENCH_REPAIRQL:
                            strings.add(runeFormat("Repair QL", entry.getValue()));
                            break;
                        case ENCH_FUELUSE:
                            strings.add(runeFormat("Fuel Use", entry.getValue()));
                            break;
                        case ENCH_ENCHANTABILITY:
                            strings.add(runeFormat("Enchant", entry.getValue()));
                            break;
                        case ENCH_ENCHRETENTION:
                            strings.add(runeFormat("Enchant Decay", -entry.getValue()));
                            break;
                        case ENCH_IMPPERCENT:
                            strings.add(runeFormat("Improve", entry.getValue()));
                            break;
                        case ENCH_RESGATHERED:
                            strings.add(runeFormat("Gathering", entry.getValue()));
                            break;
                        case ENCH_FARMYIELD:
                            strings.add(runeFormat("Farming", entry.getValue()));
                            break;
                        case ENCH_RARITYIMP:
                            strings.add(runeFormat("Rarity", entry.getValue()));
                            break;
                        case ENCH_GLOW:
                            strings.add(runeFormat("Glow", entry.getValue()));
                            break;
                    }
                }

                if (strings.size() > 0)
                    runeStrings.put(rune.getKey(), strings);
            }
        }
    }

    private static String shortName(String name) {
        String[] parts = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (part.toLowerCase().equals("of") || part.toLowerCase().equals("the"))
                sb.append(part.substring(0, 1).toLowerCase());
            else
                sb.append(part.substring(0, 1).toUpperCase());
        }
        return sb.toString();
    }

}
