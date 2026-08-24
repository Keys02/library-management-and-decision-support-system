package test.greedy;

import algorithms.greedy.RequestPriorityScheduler;
import model.RequestStatus;
import model.RequestType;
import model.ServiceRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class RequestPrioritySchedulerTest {

    public static void main(String[] args) {

        List<ServiceRequest> requests = Arrays.asList(

                new ServiceRequest(
                        1,
                        101,
                        1001,
                        RequestType.BORROW,
                        5,
                        RequestStatus.PENDING,
                        LocalDateTime.now().minusHours(5)
                ),

                new ServiceRequest(
                        2,
                        102,
                        1002,
                        RequestType.RETURN,
                        3,
                        RequestStatus.PENDING,
                        LocalDateTime.now().minusHours(10)
                ),

                new ServiceRequest(
                        3,
                        103,
                        1003,
                        RequestType.RESERVE,
                        5,
                        RequestStatus.PENDING,
                        LocalDateTime.now().minusHours(2)
                )

        );

        RequestPriorityScheduler scheduler =
                new RequestPriorityScheduler();

        List<ServiceRequest> ordered =
                scheduler.schedule(requests);

        for (ServiceRequest request : ordered) {

            System.out.println(
                    "Request "
                            + request.getId()
                            + " Priority: "
                            + request.calculatePriority()
            );

        }

    }

}