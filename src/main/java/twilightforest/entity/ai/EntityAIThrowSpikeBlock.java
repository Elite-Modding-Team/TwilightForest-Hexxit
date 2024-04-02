package twilightforest.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import twilightforest.entity.EntityTFBlockGoblin;
import twilightforest.entity.EntityTFSpikeBlock;

public class EntityAIThrowSpikeBlock extends EntityAIBase {

    protected EntityTFBlockGoblin attacker;
    protected EntityTFSpikeBlock spikeBlock;
    private int cooldown;

    public EntityAIThrowSpikeBlock(EntityTFBlockGoblin entityTFBlockGoblin, EntityTFSpikeBlock entitySpikeBlock) {
        this.attacker = entityTFBlockGoblin;
        this.spikeBlock = entitySpikeBlock;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        this.cooldown = Math.max(0, this.cooldown - 1);
        EntityLivingBase target = this.attacker.getAttackTarget();
        if (target == null || this.attacker.getDistanceSq(target) > 42 || this.cooldown > 0) {
            this.cooldown--;
            return false;
        } else {
            return this.attacker.isEntityAlive() && this.attacker.canEntityBeSeen(target);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.attacker.getChainMoveLength() > 0;
    }

    @Override
    public void startExecuting() {
        this.attacker.setThrowing(true);
        this.cooldown = 100 + this.attacker.world.rand.nextInt(100);
    }

}
