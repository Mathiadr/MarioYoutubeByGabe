package brunostEngine;

import components.Animation;
import components.Animator;
import components.Component;
import components.Spritesheet;
import physics2d.components.CylinderCollider;
import physics2d.components.Rigidbody2D;
import physics2d.enums.BodyType;

import java.util.ArrayList;

public class Character extends Component {

    public static GameObject createSimpleCharacter(CharacterBuilder characterBuilder){
        String name = characterBuilder.getName();
        Spritesheet characterSpritesheet = characterBuilder.getSpritesheet(0.25f, 0.25f);
        GameObject character = AssetBuilder.generateSpriteObject(characterSpritesheet.getSprite(0), 0.25f, 0.25f);
        character.name = name;

        ArrayList<Animation> animations = characterBuilder.getAnimations();
        Animator animator = new Animator();
        for (Animation animation : animations)
            animator.addState(animation);
        for (Animation stateFrom : animations){
            for (Animation stateTo : animations){
                animator.addState(stateFrom.title, stateTo.title, stateTo.title);
            }
        }
        character.addComponent(animator);

        CylinderCollider cylinderCollider = new CylinderCollider();
        cylinderCollider.width = 0.21f;
        cylinderCollider.height = 0.25f;
        character.addComponent(cylinderCollider);

        Rigidbody2D rigidbody2D = new Rigidbody2D();
        rigidbody2D.setBodyType(BodyType.Dynamic);
        rigidbody2D.setContinuousCollision(false);
        rigidbody2D.setFixedRotation(true);
        rigidbody2D.setMass(25.0f);
        rigidbody2D.setGravityScale(1.0f);
        if (!characterBuilder.gravityEnabled())
            rigidbody2D.setGravityScale(0.0f);
        character.addComponent(rigidbody2D);

        character.addComponent(characterBuilder.getPlayerController());

        character.transform.zIndex = 10;

        return character;
    }
}
