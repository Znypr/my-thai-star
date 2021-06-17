/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package ga.codehub.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import ga.codehub.alexa.handlers.*;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

    public static final String BASE_URL = "https://0611360415ae.ngrok.io"; // Jannik
    // public static final String BASE_URL = "https://2b455f44cfdd.ngrok.io"; // Rebecca
    // public static final String BASE_URL = "http://fb6c3671b0bc.ngrok.io"; // Candra

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                    new CancelandStopIntentHandler(), 
                    new BookingIntentHandler(),
                    new MenuIntentHandler(), 
                    new HelpIntentHandler(), 
                    new LaunchRequestHandler(),
                    new SessionEndedRequestHandler(), 
                    new OrderIntentHandler(), 
                    new OrderDialogIntentHandler(),
                    new InhouseOrderIntentHandler(),
                    new ShoppingCartIntentHandler(),
                    new FallbackIntentHandler())
                // Add your skill id below
                // .withSkillId("amzn1.ask.skill.f5fa69a6-0822-4a5f-b959-ad2893865a9e") //Rebecca at home
                // .withSkillId("amzn1.ask.skill.b849ee87-c5b0-4a88-ba30-5b823f89ddea") // Candra
                // .withSkillId("amzn1.ask.skill.7d4334f3-c8eb-4fe3-9542-2ebb07ad96e1") //Rebecca inhouse
                .withSkillId("amzn1.ask.skill.eb6454aa-22a7-4a01-b0c1-6f7b07caee24") //Jannik
                .build();
    }

    public MyThaiStarStreamHandler() {
        super(getSkill());
    }

}
