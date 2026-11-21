package org.firstinspires.ftc.teamcode.config;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name="manual")
public class manual extends LinearOpMode {

    private DcMotor intake, transfer;
    private DcMotor lf, rf, lr, rr;
    private IMU imu;

    // Intake reverse timing
    @Override
    public void runOpMode() throws InterruptedException {

        // Hardware map
        lf = hardwareMap.get(DcMotor.class, "lf");
        rf = hardwareMap.get(DcMotor.class, "rf");
        lr = hardwareMap.get(DcMotor.class, "lr");
        rr = hardwareMap.get(DcMotor.class, "rr");
        intake = hardwareMap.get(DcMotor.class, "intake");
        transfer = hardwareMap.get(DcMotor.class, "transfer");


        intake.setDirection(DcMotor.Direction.REVERSE);
        transfer.setDirection(DcMotor.Direction.REVERSE);
        lf.setDirection(DcMotor.Direction.REVERSE);
        lr.setDirection(DcMotor.Direction.REVERSE);

        //        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //        lr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //        rr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters params = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
        );
        imu.initialize(params);


        waitForStart();

        imu.resetYaw();

        while (opModeIsActive()) {

            if (gamepad1.left_bumper) {
                imu.resetYaw();
            }


            double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

            // ----- DRIVETRAIN (Field-Centric) -----
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x * 0.8;

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

            double intakePower = -gamepad2.left_stick_y;

            if (Math.abs(intakePower) > 0.1) {
                intake.setPower(intakePower);
                transfer.setPower(intakePower);
            } else {
                intake.setPower(0);
                transfer.setPower(0);
            }


        }
    }
}