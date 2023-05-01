package twilightforest.tileentity.spawner;

import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;

public class TileEntityTFFinalBossSpawner extends TileEntityTFBossSpawner {

    public TileEntityTFFinalBossSpawner() {
        super(EntityList.getKey(EntityWroughtnaut.class));
    }

    @Override
    protected void initializeCreature(EntityLiving myCreature) {
        super.initializeCreature(myCreature);
        double health = myCreature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue();
        myCreature.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health * 4);
        myCreature.setHealth((float) (health * 4));
        double speed = myCreature.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue();
        myCreature.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed * 1.5);
        myCreature.setGlowing(true);
    }
}
