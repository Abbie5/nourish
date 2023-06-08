package dev.emi.nourish.mixin;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.emi.nourish.effects.NourishStatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {

	@Inject(at = @At("RETURN"), method = "getDurationText", cancellable = true)
	private static void durationToString(StatusEffectInstance inst, float f, CallbackInfoReturnable<Text> info) {
		if (inst instanceof NourishStatusEffectInstance effect) {
			if (effect.getEffectType().isBeneficial()) {
				info.setReturnValue(Text.literal("Nourished"));
			} else {
				info.setReturnValue(Text.literal("Malnourished"));
			}
		}
	}
}