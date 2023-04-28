package components;

public class FontRenderer extends Component {

    @Override
    public void onStart() {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found Font Renderer!");
        }
    }

    @Override
    public void onUpdate(float dt) {

    }
}
