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

import ga.codehub.alexa.handlers.AskForHelpIntentHandler;
import ga.codehub.alexa.handlers.BookingIntentHandler;
import ga.codehub.alexa.handlers.CancelandStopIntentHandler;
import ga.codehub.alexa.handlers.FallbackIntentHandler;
import ga.codehub.alexa.handlers.HelpIntentHandler;
import ga.codehub.alexa.handlers.InformationIntentHandler;
import ga.codehub.alexa.handlers.InhouseOrderIntentHandler;
import ga.codehub.alexa.handlers.LaunchRequestHandler;
import ga.codehub.alexa.handlers.MenuIntentHandler;
import ga.codehub.alexa.handlers.OrderDialogIntentHandler;
import ga.codehub.alexa.handlers.OrderIntentHandler;
import ga.codehub.alexa.handlers.SessionEndedRequestHandler;
import ga.codehub.alexa.handlers.ShoppingCartIntentHandler;

public class MyThaiStarStreamHandler extends SkillStreamHandler {
  // Add your base URL below
  public static final String ALEXA_SKILL_ID = "amzn1.ask.skill.8770069f-276d-405b-b09d-63d3112e9510";

  public static final String BASE_URL = "https://bb70e0b90be0.ngrok.io/mythaistar/services/rest";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(), new BookingIntentHandler(), new MenuIntentHandler(),
            new HelpIntentHandler(), new LaunchRequestHandler(), new SessionEndedRequestHandler(),
            new OrderIntentHandler(), new OrderDialogIntentHandler(), new InhouseOrderIntentHandler(),
            new ShoppingCartIntentHandler(), new InformationIntentHandler(), new AskForHelpIntentHandler(),
            new FallbackIntentHandler())
        // Add your skill id below
        .withSkillId(ALEXA_SKILL_ID).build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }

}
