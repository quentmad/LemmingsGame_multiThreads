package lemmings.game.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class NumberRenderer {

    private final Vector2[] casePositions; //Positions des neuf cases
    private SpriteSheetInfo spriteNumbers;//Pour textures des chiffres de 0 à 9
    private int offsetX;//Afin de centré horizontalement les 2 chiffres et qu'ils ne dépassent pas sur les cotés
    private final int sizeUnit = 54;//Taille de chaque case

    public NumberRenderer() {
        this.spriteNumbers = new SpriteSheetInfo(sizeUnit, "0_9_size54");
        this.casePositions  = new Vector2[] {new Vector2(6, 143), new Vector2(101, 143), new Vector2(196, 143), new Vector2(291, 143), 
            new Vector2(387, 143), new Vector2(482, 143), new Vector2(577, 143), new Vector2(672, 143), new Vector2(767, 143),new Vector2(862, 143)
        };
        this.offsetX = 6;
    }

    /**
     * Affiche le nombre dans la case souhaité
     * @param batch
     * @param number le nombre entre 00 et 99 à afficher
     * @param caseIndex la case dans laquelle afficher le nombre (de 1 à 9)
     */
    public void drawTwoDigitNumber(SpriteBatch batch, int number, int caseIndex) {
        //Assure que le nombre est compris entre 0 et 99
        number = Math.min(Math.max(number, 0), 99);

        //Obtenir les chiffres des dizaines et des unités
        int tensDigit = number / 10;
        int onesDigit = number % 10;

        //Obtenir la position de la case
        Vector2 casePosition = casePositions[caseIndex-1];

        //Calcule la position du chiffre des dizaines pour le centrer dans la case
        float tensX = casePosition.x + (84 - sizeUnit * 2) / 2 + offsetX;
        float tensY = casePosition.y + (84 - sizeUnit) / 2;

        //Calcule la position du chiffre des unités pour le centrer dans la case
        float onesX = tensX + sizeUnit - 2*offsetX;
        float onesY = tensY;

        //Dessine les chiffres dans les positions calculées
        batch.draw(spriteNumbers.spriteSheetRegions()[0][tensDigit], tensX, tensY);
        batch.draw(spriteNumbers.spriteSheetRegions()[0][onesDigit], onesX, onesY);

    }


    /**
     * Affiche le nombre dans la case souhaité
     * @param batch
     * @param number le nombre entre 00 et 99 à afficher
     * @param caseIndex la case dans laquelle afficher le nombre (de 1 à 9)
     */
    public void drawAllTwoDigitNumber(SpriteBatch batch, int[] numbersSkills){
        for(int i = 0; i < numbersSkills.length; i++){
            if(i != 3){// Le skill blockeur n'ayant pas ete implementé, on ne l'affiche pas
            drawTwoDigitNumber(batch, numbersSkills[i], i+2);
            }
        }
    }


}
