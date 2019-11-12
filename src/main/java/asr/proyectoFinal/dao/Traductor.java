package asr.proyectoFinal.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v3.util.Language;

import asr.proyectoFinal.dao.VCAPHelper;

import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslationResult;

public class Traductor {
	public static String translate(String palabra) {
        
        String url;
        String api;

		if (System.getenv("VCAP_SERVICES") != null) {
			// When running in Bluemix, the VCAP_SERVICES env var will have the credentials for all bound/connected services
			// Parse the VCAP JSON structure looking for cloudant.
			JsonObject translateCredentials = VCAPHelper.getCloudCredentials("language_translator");
			if(translateCredentials == null){
				System.out.println("No translation service bound to this application");
				return null;
			}
            url = translateCredentials.get("url").getAsString();
            api = translateCredentials.get("apikey").getAsString();
		} else {
			System.out.println("Running locally. Looking for credentials in translate.properties");
            url = VCAPHelper.getLocalProperties("translate.properties").getProperty("translate_url");
            api = VCAPHelper.getLocalProperties("translate.properties").getProperty("translate_api");

			if(url == null || url.length()==0){
				System.out.println("To use translation, set the Translation url endpoint and API key in src/main/resources/translate.properties");
				return null;
			}
		}
        
        try {
            // Authentication
            LanguageTranslator service = new LanguageTranslator("2018-05-01");
            service.setEndPoint(url);
            service.setUsernameAndPassword("apikey",api);
            
            // Options for translation
            TranslateOptions translateOptions = new TranslateOptions.Builder()
            .addText(palabra)
            .source(Language.SPANISH)
            .target(Language.ENGLISH)
            .build();
            
            // Translate word
            TranslationResult translationResult = service.translate(translateOptions).execute();

            // Log result of request
            System.out.println(translationResult);

            // JSON parsing
            String traduccionJSON = translationResult.toString();
            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(traduccionJSON).getAsJsonObject();
            
            // JSON access
            JsonArray traducciones = rootObj.getAsJsonArray("translations");
            String traduccionPrimera = palabra;
            
            if(traducciones.size() > 0)
                traduccionPrimera = traducciones.get(0).getAsJsonObject().get("translation").getAsString();
            
            return traduccionPrimera;

        } catch(Exception e) {

            System.out.println("Unable to connect to database");
			//e.printStackTrace();
            return null;
        }
	}
}
