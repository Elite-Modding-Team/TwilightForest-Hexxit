package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;

public class EntityAITFLichWatchTarget extends EntityAIBase {

    private final EntityMob entity;

    public EntityAITFLichWatchTarget(EntityMob entity) {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null;
    }

    @Override
    public void updateTask() {
        super.updateTask();

        if (this.entity.getAttackTarget() != null) {
            this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 100.0F, 100.0F);
        }
    }
}
