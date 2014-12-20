package cz.tomasdvorak.czechpoints;

import cz.tomasdvorak.czechpoints.workflow.Workflow;

public class UpdateCzechpoints {

    public static void main(String[] args) throws Exception {

        if (!Boolean.getBoolean("updateData")) {
            System.out.println("Skipping data update, 'updateData' parameter not present");
            return;
        }

        new Workflow().start();
    }
}
