package algorithms.greedy;

import model.ServiceRequest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RequestPriorityScheduler {

    public List<ServiceRequest> schedule(
            List<ServiceRequest> requests) {

        List<ServiceRequest> sorted =
                new ArrayList<>(requests);

        sorted.sort(
                Comparator
                        .comparingInt(ServiceRequest::calculatePriority)
                        .reversed()
                        .thenComparing(ServiceRequest::getCreatedAt)
        );

        return sorted;
    }

}