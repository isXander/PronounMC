package cc.woverflow.pronounmc.utils;

import net.minecraft.text.TranslatableText;

public enum Pronouns {
    UNSPECIFIED("unspecified"),
    HE_HIM("hh"),
    HE_IT("hi"),
    HE_SHE("hs"),
    HE_THEY("ht"),
    IT_HIM("ih"),
    IT_ITS("ii"),
    IT_SHE("is"),
    IT_THEY("it"),
    SHE_HE("shh"),
    SHE_HER("sh"),
    SHE_IT("si"),
    SHE_THEY("st"),
    THEY_HE("th"),
    THEY_IT("ti"),
    THEY_SHE("ts"),
    THEY_THEM("tt"),
    ANY("any"),
    OTHER("other"),
    ASK("ask"),
    AVOID("avoid");

    public final String id;

    Pronouns(String id) {
        this.id = id;
    }

    public TranslatableText getText() {
        return new TranslatableText("pronounmc.pronoun." + id);
    }

    public static Pronouns fromId(String id) {
        for (Pronouns p : values()) {
            if (p.id.equals(id)) {
                return p;
            }
        }

        throw new IllegalArgumentException("No pronoun with id " + id);
    }
}
