package com.ss174h.amsa.CheckPermissions;

public class AppCategories {

    public String checkCategory(String category, String permissions) {
        String response = "";
        if(category == null) {
            response = "Unable to identify the category for the application.  If the dangerous permissions above seem excessive for the type of app it is, exercise caution.\n";
        } else if(category.contains("ART_AND_DESIGN")) {
            response = art(permissions);
        } else if(category.contains("AUTO_AND_VEHICLES")) {
            response = auto(permissions);
        } else if(category.contains("BEAUTY")) {
            response = beauty(permissions);
        } else if(category.contains("BOOKS_AND_REFERENCE")) {
            response = books(permissions);
        } else if(category.contains("BUSINESS")) {
            response = business(permissions);
        } else if(category.contains("COMICS")) {
            response = comics(permissions);
        } else if(category.contains("COMMUNICATION")) {
            response = comms(permissions);
        } else if(category.contains("DATING")) {
            response = dating(permissions);
        } else if(category.contains("EDUCATION")) {
            response = education(permissions);
        } else if(category.contains("ENTERTAINMENT")) {
            response = entertainment(permissions);
        } else if(category.contains("EVENTS")) {
            response = events(permissions);
        } else if(category.contains("FINANCE")) {
            response = finance(permissions);
        } else if(category.contains("FOOD_AND_DRINK")) {
            response = food(permissions);
        } else if(category.contains("HEALTH_AND_FITNESS")) {
            response = health(permissions);
        } else if(category.contains("HOUSE_AND_HOME")) {
            response = house(permissions);
        } else if(category.contains("LIFESTYLE")) {
            response = lifestyle(permissions);
        } else if(category.contains("MAPS_AND_NAVIGATION")) {
            response = maps(permissions);
        } else if(category.contains("MEDICAL")) {
            response = medical(permissions);
        } else if(category.contains("MUSIC_AND_AUDIO")) {
            response = music(permissions);
        } else if(category.contains("NEWS_AND_MAGAZINES")) {
            response = news(permissions);
        } else if(category.contains("PARENTING")) {
            response = parenting(permissions);
        } else if(category.contains("PERSONALIZATION")) {
            response = personalization(permissions);
        } else if(category.contains("PHOTOGRAPHY")) {
            response = photography(permissions);
        } else if(category.contains("PRODUCTIVITY")) {
            response = productivity(permissions);
        } else if(category.contains("SHOPPING")) {
            response = shopping(permissions);
        } else if(category.contains("SOCIAL")) {
            response = social(permissions);
        } else if(category.contains("SPORTS")) {
            response = sports(permissions);
        } else if(category.contains("TOOLS")) {
            response = tool(permissions);
        } else if(category.contains("TRAVEL_AND_LOCAL")) {
            response = travel(permissions);
        } else if(category.contains("VIDEO_PLAYERS")) {
            response = video(permissions);
        } else if(category.contains("WEATHER")) {
            response = weather(permissions);
        } else if(category.contains("LIBRARIES_AND_DEMO")) {
            response = lib(permissions);
        } else if(category.contains("GAME_ARCADE") || category.contains("GAME_PUZZLE") || category.contains("GAME_CARD") || category.contains("GAME_CASUAL")
                || category.contains("GAME_RACING") || category.contains("GAME_SPORTS") || category.contains("GAME_ACTION") || category.contains("GAME_ADVENTURE")
                || category.contains("GAME_BOARD") || category.contains("GAME_CASINO") || category.contains("GAME_EDUCATIONAL") || category.contains("GAME_MUSIC")
                || category.contains("GAME_ROLE_PLAYING") || category.contains("GAME_SIMULATION") || category.contains("GAME_STRATEGY") || category.contains("GAME_TRIVIA")
                || category.contains("GAME_WORD")) {
            response = game(permissions);
        }
        return response;
    }

    private String art(String permissions) {
        String response;
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response = "As this application is an Art & Design application, there is a reasonable chance that it requires external storage related permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        } else {
            response = "As this application is an Art & Design application, these permissions seem to be unnecessary for it's category.\n";
        }

        return response;
    }

    private String auto(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is an Auto & Vehicles application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is an Auto & Vehicles application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else {
            response += "As this application is an Auto & Vehicles application, these permissions may be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String beauty(String permissions) {
        String response = "";
        if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Beauty application, there is a reasonable chance that it requires camera access permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        } else {
            response += "As this application is a Beauty application, these permissions may be unnecessary for it's category.\n";
        }

        return response;
    }

    private String books(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Books & Reference application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Books & Reference application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is an Books & Reference application, these permissions may be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String business(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Business application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Business application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is a Business application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else {
            response += "As this application is a Business application, these permissions may be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String comics(String permissions) {
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Comics application, there is a reasonable chance that it requires external storage related permissions. Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        } else {
            response += "As this application is a Comics application, these permissions seem to be unnecessary for it's category.\n";
        }

        return response;
    }

    private String comms(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        }  else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires camera access permissions.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else if(permissions.contains("This app can see the details of your contacts.\n") ||
                permissions.contains("This app can write information to your contacts.\n")) {
            response += "As this application is a Communication application, there is a reasonable chance that it requires contacts related permissions.\n";
            count++;
        } else {
            response += "As this application is a Communication application, these permissions seem to be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String dating(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Dating application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Dating application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is a Dating application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Dating application, there is a reasonable chance that it requires camera access permissions.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Dating application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else {
            response += "As this application is a Dating application, these permissions seem to be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String education(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is an Education application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is an Education application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is an Education application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is an Education application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else {
            response += "As this application is an Education application, these permissions seem to be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String entertainment(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is an Entertainment application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is an Entertainment application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is an Entertainment application, there is a reasonable chance that it requires camera access permissions.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is an Entertainment application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else {
            response += "As this application is an Entertainment application, these permissions seem to be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String events(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is an Events application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is an Events application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is an Events application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is an Events application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is an Entertainment application, these permissions seem to be unnecessary for it's category.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String finance(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Finance application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Finance application, there is a reasonable chance that it requires camera access permissions.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Finance application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String food(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Food & Drink application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is a Food & Drink application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is an Food & Drink application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is a Food & Drink application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String health(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Health & Fitness application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view information (e.g. heart-rate) from any body sensors you may use.\n")) {
            response += "As this application is a Health & Fitness application, there is a reasonable chance that it requires body-sensor related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Health & Fitness application, there is a reasonable chance that it requires camera related permissions.\n";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String house(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a House & Home application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a House & Home application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is an House & Home application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can send SMS messages.\n") ||
                permissions.contains("This app can receive SMS messages.\n") || permissions.contains("This app can read your SMS messages.\n")) {
            response += "As this application is a House & Home application, there is a reasonable chance that it requires SMS related permissions.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a House & Home application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        }  else {
            response += "As this application is a House & Home application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }

        return response;
    }

    private String lifestyle(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Lifestyle application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Lifestyle application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Lifestyle application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Lifestyle application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        }  else {
            response += "As this application is a Lifestyle application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String maps(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Maps & Navigation application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Maps & Navigation application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Maps & Navigation application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Maps & Navigation application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is a Maps & Navigation application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String medical(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Medical application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Medical application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Medical application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Medical application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is a Medical application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String music(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Music application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Music application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Music application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Music application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else {
            response += "As this application is a Music application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String news(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a News & Magazines application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a News & Magazines application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a News & Magazines application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else {
            response += "As this application is a News & Magazines application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String parenting(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Parenting application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Parenting application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Parenting application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Parenting application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can send SMS messages.\n") ||
                permissions.contains("This app can receive SMS messages.\n") || permissions.contains("This app can read your SMS messages.\n")) {
            response += "As this application is a Parenting application, there is a reasonable chance that it requires SMS related permissions.\n";
            count++;
        } else {
            response += "As this application is a Parenting application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String personalization(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Personalization application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else {
            response += "As this application is a Personalization application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String photography(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Photography application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Photography application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Photography application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Photography application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else {
            response += "As this application is a Photography application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String productivity(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Productivity application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Productivity application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Productivity application, there is a reasonable chance that it requires camera related permissions.\n";
        } else {
            response += "As this application is a Productivity application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String shopping(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Shopping application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Shopping application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Shopping application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Shopping application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else {
            response += "As this application is a Shopping application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String social(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can send SMS messages.\n") ||
                permissions.contains("This app can receive SMS messages.\n") || permissions.contains("This app can read your SMS messages.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires SMS related permissions.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else if(permissions.contains("This app can see the details of your contacts.\n")) {
            response += "As this application is a Social application, there is a reasonable chance that it requires contacts access permissions.\n";
        } else {
            response += "As this application is a Social application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String sports(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Sports application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Sports application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Sports application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Sports application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can view information (e.g. heart-rate) from any body sensors you may use.\n")) {
            response += "As this application is a Sports application, there is a reasonable chance that it requires body-sensor related permissions.\n";
            count++;
        } else {
            response += "As this application is a Sports application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String tool(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else if(permissions.contains("This app can see the details of your contacts.\n")) {
            response += "As this application is a Tools application, there is a reasonable chance that it requires contacts access permissions.\n";
        } else {
            response += "As this application is a Tools application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String travel(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Travel & Local application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Travel & Local application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Travel & Local application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Travel & Local application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can view your calendar.\n") ||
                permissions.contains("This app can write to your calendar.\n")) {
            response += "As this application is a Travel & Local application, there is a reasonable chance that it requires calendar related permissions.\n";
            count++;
        } else {
            response += "As this application is a Travel & Local application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String video(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Video Players & Editors application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Video Players & Editors application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Video Players & Editors application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        } else {
            response += "As this application is a Video Players & Editors application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String weather(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Weather application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Weather application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Weather application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else {
            response += "As this application is a Weather application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String lib(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Libraries & Demo application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Libraries & Demo application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Libraries & Demo application, there is a reasonable chance that it requires camera related permissions.\n";
        } else {
            response += "As this application is a Libraries & Demo application, these permissions seem to be unnecessary for it's operation.";
        }

        if(count > 0) {
            response += "Any other listed dangerous permissions may be unnecessary for it's operation.\n";
        }
        return response;
    }

    private String game(String permissions) {
        int count = 0;
        String response = "";
        if(permissions.contains("This app can view your precise location.\n")
                || permissions.contains("This app can view your approximate location.\n")) {
            response += "As this application is a Game application, there is a reasonable chance that it requires location related permissions.\n";
            count++;
        } else if(permissions.contains("This app can see a list of user accounts stored on this device.\n")) {
            response += "As this application is a Game application, there is a reasonable chance that it requires access to user accounts stored on the device.\n";
            count++;
        } else if(permissions.contains("This app can read from your external storage.\n")
                || permissions.contains("This app can write information to your external storage.\n")) {
            response += "As this application is a Game application, there is a reasonable chance that it requires external storage related permissions.\n";
            count++;
        } else if(permissions.contains("This app can access your camera.\n")) {
            response += "As this application is a Game application, there is a reasonable chance that it requires camera related permissions.\n";
        } else if(permissions.contains("This app can record audio through your device.\n")) {
            response += "As this application is a Game application, there is a reasonable chance that it requires microphone access permissions.\n";
            count++;
        }

        return response;
    }
}
