package twilightforest.mixin;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = EntityWroughtnaut.class, remap = false)
public class TFEntityWroughtnautMixin {
    @ModifyConstant(method = "isAtRestPos", constant = @Constant(doubleValue = 36))
    private double utWroughtnautRestPos(double constant) {
        return 100;
    }
}
