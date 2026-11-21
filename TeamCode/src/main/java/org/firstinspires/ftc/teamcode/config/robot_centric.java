package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class robot_centric extends LinearOpMode {
    private DcMotor lf, rf, lr, rr;

        @Override
        public void runOpMode() throws InterruptedException {

            lf = hardwareMap.get(DcMotor.class, "lf");
            rf = hardwareMap.get(DcMotor.class, "rf");
            lr = hardwareMap.get(DcMotor.class, "lr");
            rr = hardwareMap.get(DcMotor.class, "rr");

            lf.setDirection(DcMotor.Direction.REVERSE);
            lr.setDirection(DcMotor.Direction.REVERSE);


            waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            lf.setPower(frontLeftPower);
            lr.setPower(backLeftPower);
            rf.setPower(frontRightPower);
            rr.setPower(backRightPower);
        }
    }
}