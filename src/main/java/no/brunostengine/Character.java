package no.brunostengine;

import no.brunostengine.components.Animation;
import no.brunostengine.components.Animator;
import no.brunostengine.components.Component;
import no.brunostengine.components.Spritesheet;
import no.brunostengine.physics.components.CapsuleCollider;
import no.brunostengine.physics.components.Rigidbody;
import no.brunostengine.physics.enums.BodyType;

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

        CapsuleCollider capsuleCollider = new CapsuleCollider();
        capsuleCollider.width = 0.21f;
        capsuleCollider.height = 0.25f;
        character.addComponent(capsuleCollider);

        Rigidbody rigidbody = new Rigidbody();
        rigidbody.setBodyType(BodyType.Dynamic);
        rigidbody.setContinuousCollision(false);
        rigidbody.setFixedRotation(true);
        rigidbody.setMass(25.0f);
        rigidbody.setGravityScale(1.0f);
        if (!characterBuilder.gravityEnabled())
            rigidbody.setGravityScale(0.0f);
        character.addComponent(rigidbody);

        character.addComponent(characterBuilder.getPlayerController());

        character.transform.zIndex = 10;

        return character;
    }
}
