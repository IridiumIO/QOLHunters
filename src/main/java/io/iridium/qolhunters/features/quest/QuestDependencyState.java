package io.iridium.qolhunters.features.quest;

public class QuestDependencyState {
    private static String currentDep = null;

    public static String getCurrentDep() {
        return currentDep;
    }

    public static void setCurrentDep(String id) {
        currentDep = id;
    }
}
