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

    // public static final String BASE_URL = "http://0ab393438803.ngrok.io"; // Rebecca
    public static final String BASE_URL = "http://2cf8013ce1d3.ngrok.io "; // Candra


    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new BookingIntentHandler(),
                        new MenuIntentHandler(),           
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                // .withSkillId("amzn1.ask.skill.f5fa69a6-0822-4a5f-b959-ad2893865a9e") // Rebecca
                .withSkillId("amzn1.ask.skill.b849ee87-c5b0-4a88-ba30-5b823f89ddea") //Candra
                .build();
    }

    public MyThaiStarStreamHandler() {
        super(getSkill());
    }

}

