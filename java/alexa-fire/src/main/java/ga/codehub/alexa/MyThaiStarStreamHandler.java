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
    // Add your base URL below
    public static final String ALEXA_SKILL_ID = "amzn1.ask.skill.c3ce8e9c-81f3-4a53-bf3d-53db70026366";
    public static final String BASE_URL = "https://91e7b89be27b.ngrok.io/mythaistar/services/rest";

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
                        new InformationIntentHandler(),
                        new AskForHelpIntentHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                .withSkillId(ALEXA_SKILL_ID)
                .build();
    }

    public MyThaiStarStreamHandler() {
        super(getSkill());
    }

}
