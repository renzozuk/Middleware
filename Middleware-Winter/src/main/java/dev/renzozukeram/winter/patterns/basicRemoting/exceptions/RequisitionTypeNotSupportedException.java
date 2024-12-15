package dev.renzozukeram.winter.patterns.basicRemoting.exceptions;

public class RequisitionTypeNotSupportedException extends RemotingError {

    public RequisitionTypeNotSupportedException(String receivedRequisitionType, String supportedRequisitionTypes) {
        super("The requisition type " + receivedRequisitionType + " is not supported. The supported requisition types are " + supportedRequisitionTypes + ".");
    }

    public RequisitionTypeNotSupportedException(String receivedRequisitionType, String supportedRequisitionTypes, Throwable cause) {
        super("The requisition type " + receivedRequisitionType + " is not supported. The supported requisition types are " + supportedRequisitionTypes + ".", cause);
    }
}
