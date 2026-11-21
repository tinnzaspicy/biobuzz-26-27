package org.firstinspires.ftc.teamcode.config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "manualrn")
public class Dttest extends LinearOpMode {


    private IMU imu;
    private DcMotor lf, rf, lr, rr, intake;

    @Override
    public void runOpMode() throws InterruptedException {

        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lr = hardwareMap.get(DcMotor.class, "lr");
        rr = hardwareMap.get(DcMotor.class, "rr");
        intake = hardwareMap.get(DcMotor.class, "intake");
        imu = hardwareMap.get(IMU.class, "imu");

        lf.setDirection(DcMotor.Direction.REVERSE);
        lr.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        imu.initialize(new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        ));

        waitForStart();

        imu.resetYaw();

        while (opModeIsActive()) {

            double p = -gamepad2.left_stick_y;

            intake.setPower(
                    Math.abs(p) > 0.1 ? p : 0
            );

            if (gamepad1.left_bumper) {
                imu.resetYaw();
            }

            if (gamepad2.a) {
                intake.setPower(0.7);
            }


            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            double y = -gamepad1.left_stick_y; // * 0.85
            double x = gamepad1.left_stick_x;  //* 1.1;
            double rx = gamepad1.right_stick_x; // * 0.8;

            if (Math.abs(y) > 0.01 || Math.abs(x) > 0.01 || Math.abs(rx) > 0.01) {

                double rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
                double rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

                double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

                lf.setPower((rotY + rotX + rx) / denominator);
                lr.setPower((rotY - rotX + rx) / denominator);
                rf.setPower((rotY - rotX - rx) / denominator);
                rr.setPower((rotY + rotX - rx) / denominator);

            } else {
                lf.setPower(0);
                lr.setPower(0);
                rf.setPower(0);
                rr.setPower(0);
            }
            telemetry.addData("Intake motor INT", p);

            telemetry.update();
        }
    }
}
