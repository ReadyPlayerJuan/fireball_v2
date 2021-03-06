package com.fireball.game.entities.abilities;

import com.fireball.game.entities.ControllableEntity;
import com.fireball.game.entities.Entity;
import com.fireball.game.entities.player.*;
import com.fireball.game.util.DataFile;
import com.fireball.game.util.Util;

import java.util.ArrayList;

public abstract class Ability extends Entity {
    protected ControllableEntity owner;
    protected Entity castOwner;
    protected String subAbilityName;

    public Ability(String name, ControllableEntity owner, Entity castOwner, String subAbilityName, double x, double y) {
        super(owner.getTeam(), name, x, y);
        this.owner = owner;
        this.castOwner = castOwner;
        this.subAbilityName = subAbilityName;
    }

    protected void castSubAbility(CastArgumentOverride... overrides) {
        if(subAbilityName != null && !subAbilityName.equals("")) {
            castAbility(owner, this, subAbilityName, overrides);
        }
    }

    public static void castAbility(ControllableEntity owner, Entity castOwner, String abilityCastNameString, CastArgumentOverride... argumentOverrides) {
        Entity[] createdObjects;


        //simplify the name if specified
        DataFile.setCurrentLocation("abilities", owner.getName(), abilityCastNameString);
        String castName = abilityCastNameString;
        try {
            castName = DataFile.getString("cast_name");
        } catch(IllegalArgumentException e) {}
        System.out.println(owner.getName() + " cast " + abilityCastNameString + " as " + castName);


        double createX = castOwner.getX();
        double createY = castOwner.getY();
        double targetX = owner.getTargetX();
        double targetY = owner.getTargetY();
        double angle = Math.atan2(targetY - createY, targetX - createX);
        ArrayList<Object[]> extraArgs = new ArrayList<Object[]>();
        for(CastArgumentOverride override: argumentOverrides) {
            switch(override.getType()) {
                case CastArgumentOverride.ARGUMENT_OWNER:
                    castOwner = override.getOwner(); break;
                case CastArgumentOverride.ARGUMENT_CREATE_POSITION:
                    createX = override.getCreateX();
                    createY = override.getCreateY(); break;
                case CastArgumentOverride.ARGUMENT_TARGET_POSITION:
                    targetX = override.getTargetX();
                    targetY = override.getTargetY(); break;
                case CastArgumentOverride.ARGUMENT_ANGLE:
                    angle = override.getAngle(); break;
                case CastArgumentOverride.ARGUMENT_OTHER:
                    extraArgs.add(override.getOther()); break;
            }
        }
        String subAbilityName = "";
        try {
            subAbilityName = DataFile.getString("sub_ability_name");
        } catch(IllegalArgumentException e) {}


        DataFile.setCurrentLocation("abilities", owner.getName(), abilityCastNameString);
        if(castName.equals("fireball")) {
            float radius = DataFile.getFloat("radius");
            if(extraArgs.size() > 0)
                for(Object[] o: extraArgs)
                    if(o[0] instanceof String && o[0].equals("radius"))
                        radius = (float)((Double)o[1]).doubleValue();

            Entity e = new Fireball(owner,
                    castOwner,
                    subAbilityName,
                    createX,
                    createY,
                    radius,
                    angle,
                    DataFile.getFloat("velocity"),
                    DataFile.getFloat("lifetime"),
                    DataFile.getFloat("damage"),
                    DataFile.getFloat("knockback"),
                    DataFile.getFloat("stun"),
                    DataFile.getFloat("stun_friction"));
            createdObjects = new Entity[] {e};
        } else if(castName.equals("explosion")) {
            Entity e = new Explosion(owner,
                    castOwner,
                    subAbilityName,
                    createX,
                    createY,
                    DataFile.getFloat("radius"),
                    DataFile.getFloat("damage"),
                    DataFile.getFloat("knockback"),
                    DataFile.getFloat("stun"),
                    DataFile.getFloat("stun_friction"));
            createdObjects = new Entity[] {e};
        } else if(castName.equals("flamethrower")) {
            Entity e = new Flamethrower(owner,
                    castOwner,
                    subAbilityName,
                    createX,
                    createY,
                    angle,
                    DataFile.getFloat("angle_range"),
                    DataFile.getFloat("fire_rate"),
                    DataFile.getInt("num"));
            createdObjects = new Entity[] {e};
        } else if(castName.equals("flame")) {
            double lifetime = DataFile.getFloat("lifetime");
            double velocity = DataFile.getFloat("velocity") + (Math.random()*2-1) * DataFile.getFloat("velocity_range");
            Entity e = new FlamethrowerProjectile(owner,
                    castOwner,
                    subAbilityName,
                    createX,
                    createY,
                    lifetime,
                    angle,
                    velocity,
                    (DataFile.getFloat("final_velocity") - velocity)/lifetime,
                    owner.getBodyHitboxes()[0].getRadius(),
                    DataFile.getFloat("radius"),
                    DataFile.getFloat("grow_time"));
            createdObjects = new Entity[] {e};
        } else if(castName.equals("ring") || castName.equals("temp_ring")) {
            int num = DataFile.getInt("num");
            createdObjects = new Entity[num];

            float minLifetime = DataFile.getFloat("min_lifetime");
            float maxLifetime = DataFile.getFloat("max_lifetime");
            ArrayList<Float> lifetimes = new ArrayList<Float>();
            for(int i = 0; i < num; i++) {
                lifetimes.add((int)(Math.random() * (lifetimes.size()+1)), (float)Util.mix(minLifetime, maxLifetime, (float)i/(num-1)));
            }

            for(int i = 0; i < num; i++) {
                Entity e = new RingFireball(owner,
                        castOwner,
                        subAbilityName,
                        (AbilityCooldown) (extraArgs.get(0)[0]),
                        createX,
                        createY,
                        DataFile.getFloat("radius"),
                        lifetimes.get(i),
                        i * (Math.PI * 2 / num),
                        DataFile.getFloat("spin_speed"),
                        0,
                        DataFile.getFloat("max_distance"),
                        DataFile.getFloat("extend_time"));
                createdObjects[i] = e;
            }
        } else if(castName.equals("laser")) {
            Entity e = new Laser(owner,
                    castOwner,
                    subAbilityName,
                    createX,
                    createY,
                    DataFile.getFloat("radius"),
                    DataFile.getFloat("extend_speed"));
            createdObjects = new Entity[] {e};
        }
    }
}
