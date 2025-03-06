package net.altosheeve.soprano.client.Tuba.Async;

import java.util.ArrayList;

public class Fufillment extends Request {
    private ArrayList<Request> requests = new ArrayList<>();
    public Fufillment(Request completionRoutine) {
        super(null, completionRoutine.runtimeTask); //runtime execution method is overriden instead of passed as a lambda here
    }
    public Fufillment(ArrayList<Request> requests, Request completionRoutine) {
        super(null, completionRoutine.runtimeTask); //runtime execution method is overriden instead of passed as a lambda here
        this.requests = requests;
    }
    public void addRequest(Request request) { this.requests.add(request); }
    public void addRequests(ArrayList<Request> requests) { this.requests.addAll(requests); }
    //requests should be able to be added at any point in fulfillment runtime
    @Override
    public boolean exec() {
        for (int i = requests.size() - 1; i >= 0; i--) {
            if (requests.get(i).exec()) requests.remove(i); //if a request has finished executing, remove it from the fufillment pool
        }
        return requests.isEmpty(); //if the fulfillment pool is empty, that means it's complete, Meaning its completion routine can finally run.
    }
}
