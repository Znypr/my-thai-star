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

public class MyThaiStartStreamHandler extends SkillStreamHandler {

    public static final String BASE_URL = "https://c19f6a734ce9.ngrok.io";


    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new ga.codehub.alexa.handlers.CancelandStopIntentHandler(),
                        new BookingIntentHandler(),
                        new MenueIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.c3ce8e9c-81f3-4a53-bf3d-53db70026366")
                .build();
    }

    public MyThaiStartStreamHandler() {
        super(getSkill());
    }

}
