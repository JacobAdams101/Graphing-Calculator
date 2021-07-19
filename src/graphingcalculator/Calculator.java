
package graphingcalculator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * This class is the really important one like fr this one is the super important one so don't mess this up Jacob
 * it bascially contains lots of other classes to be able to describe most of algebra up to an a level further mathematics standard
 * it includes expressions, equations, matricies (which can also by implementation be used as vectors) and graphs which can be used generally to plot lines, areas (for inequalities), complex number points (argand diagrams), and shapes (where the points are described by a matrix)
 * 
 * @author jacob
 */
public class Calculator
{
    /*
    ACCURACY VALUES
    USED IN THE PROGRAM TO COMBAT FLOATING POINT ERROS AND FOR SPEEDING UP BEAFY NON EXACT TASKS SUCH AS GRAPHING
    */
    public static final double BASEDISPLAYACCURACY = 10; //The number of digits the program will natrually display too
    /**
     * The accuracy required for a number to be considered equivalent to another, mostly used for pi, e and other common constants but also used in the equals() query 
     */
    public static final double TRANSCENDENTALINACCURACY = Math.pow(10, -BASEDISPLAYACCURACY);
    
    /**
     * The accuracy value for root finding with Newton's method
     */
    public static final double NEWTONSMETHODACCURACY = 0.0000001;
    /**
     * The accuracy value for which two roots are considered approximations of the same root
     * This accuracy value is double (half as accurate) as the iteration/NEWTONSMETHODACCURACY one so it is impossible for one root to be considered two
     */
    public static final double ROOTSEQUALACCURACY = NEWTONSMETHODACCURACY*2.0;
    /**
     * This is a much lower accuracy value used for less precise, heavy load features such as graphing
     */
    public static final double GRAPHINGACCURACY = 0.005;
    /**
     * This is a medium level accuracy used for finding intercepts
     */
    public static final double INTERCEPTSACCURACY = GRAPHINGACCURACY*0.2;
    /**
     * This is a medium level accuracy used for deciding if two roots are actually the same root
     */
    public static final double INTERCEPTSEQUALACCURACY = INTERCEPTSACCURACY*5.0;
    
    
    /*
    ITERATIVE VALUES
    USED TO CONTROL ALL THE ITERATIVE PROCESSES IN THE CODE
    */
    /**
     * The maximum number of iterations for Newton's method and any other iterative processes before termination
     */
    public static final int ITERATIONCOUNT = 10000;
    
    //Iteration starting Values
    /**
     * The lowest real starting value
     */
    public static final double REALMIN = -20;
    /**
     * The highest real starting value
     */
    public static final double REALMAX = 20;
    /**
     * The lowest imaginary starting value
     */
    public static final double IMGMIN = -20;
    /**
     * The highest imaginary starting value
     */
    public static final double IMGMAX = 20;
    /**
     * The step size (gap between) starting values on the real axis
     */
    public static final double REALSTEP = 1;
    /**
     * The step size (gap between) starting values on the imaginary axis
     */
    public static final double IMGSTEP = 5;
    
    
    
    /*
    GRAPHICAL VALUES
    USED TO CONTROL THE GRAPHING ASPECT OF MY PROGRAM
    */
    public static final int AAAMOUNT = 6;
    /**
     * The width of the graphing brush/line
     */
    public static final int BRUSHWIDTH = 6;
    
    /**
     * NOT USED YET
     * 
     * Will be use to replace the double datatype in this program so that it can support complex numbers
     * 
     * Class is immutable
     * 
     * MORE FUNCTIONS ARE NEEDED TO BE ADDED BEFORE IT WILL WORK HOWEVER
     */
    public static class Complex
    {
        public final double REAL; //Stores the real component of the complex number
        
        public final double IMAGINARY; //Stores the imaginary part of the complex number
        
        public final OtherValues UNDEFINED; //Stores if the value is undefined or infinity
        
        public enum OtherValues
        {
            UNDEFINED,
            PLUSINFINITY,
            MINUSINFINITY,
        }
        
        public Complex()
        {
            this.REAL = 0;
            this.IMAGINARY = 0;
            this.UNDEFINED = null;
        }
        public Complex(double real)
        {
            this.REAL = real;
            this.IMAGINARY = 0;
            this.UNDEFINED = null;
        }
        public Complex(double real, double imaginary)
        {
            this.REAL = real;
            this.IMAGINARY = imaginary;
            this.UNDEFINED = null;
        }
        public Complex(OtherValues undefined)
        {
            this.REAL = 0;
            this.IMAGINARY = 0;
            this.UNDEFINED = undefined;
        }
        /**
         * Adds two complex numbers together
         * @param x
         * @return 
         */
        public Complex add(Complex x)
        {
            if (this.UNDEFINED != null || x.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x.UNDEFINED == OtherValues.MINUSINFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x.UNDEFINED == OtherValues.PLUSINFINITY))
                {
                    return new Complex(OtherValues.UNDEFINED); //Inf - Inf = Undefined
                }
                if (this.UNDEFINED == OtherValues.PLUSINFINITY || x.UNDEFINED == OtherValues.PLUSINFINITY)
                {
                    return new Complex(OtherValues.PLUSINFINITY); //Inf + x = Inf
                }
                if (this.UNDEFINED == OtherValues.MINUSINFINITY || x.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    return new Complex(OtherValues.MINUSINFINITY); //x - Inf = -Inf
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(this.REAL + x.REAL, this.IMAGINARY + x.IMAGINARY);
        }
        /**
         * Subtracts one complex number from another
         * @param x
         * @return 
         */
        public Complex sub(Complex x)
        {
            if (this.UNDEFINED != null || x.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x.UNDEFINED == OtherValues.PLUSINFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(OtherValues.UNDEFINED); //Inf - Inf = Undefined
                }
                if (this.UNDEFINED == OtherValues.PLUSINFINITY || x.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    return new Complex(OtherValues.PLUSINFINITY); //Inf + x = Inf
                }
                if (this.UNDEFINED == OtherValues.MINUSINFINITY || x.UNDEFINED == OtherValues.PLUSINFINITY)
                {
                    return new Complex(OtherValues.MINUSINFINITY); //x - Inf = -Inf
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(this.REAL - x.REAL, this.IMAGINARY - x.IMAGINARY);
        }
        /**
         * Multiplies two complex numbers together
         * @param x
         * @return 
         */
        public Complex mult(Complex x)
        {
            if (this.UNDEFINED != null || x.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x.UNDEFINED == OtherValues.PLUSINFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x.UNDEFINED == OtherValues.MINUSINFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x.UNDEFINED == OtherValues.PLUSINFINITY))
                {
                    return new Complex(OtherValues.MINUSINFINITY);
                }
                if (this.UNDEFINED == OtherValues.PLUSINFINITY || x.UNDEFINED == OtherValues.PLUSINFINITY)
                {
                    if (this.isZero() || x.isZero())
                    {
                        return new Complex(OtherValues.UNDEFINED);
                    }
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if (this.UNDEFINED == OtherValues.MINUSINFINITY || x.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    if (this.isZero() || x.isZero())
                    {
                        return new Complex(OtherValues.UNDEFINED);
                    }
                    return new Complex(OtherValues.MINUSINFINITY);
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex((this.REAL * x.REAL) - (this.IMAGINARY * x.IMAGINARY), (this.IMAGINARY * x.REAL) + (this.REAL * x.IMAGINARY));
        }
        /**
         * Divides one complex number from another
         * @param x
         * @return 
         */
        public Complex div(Complex x)
        {
            if (this.UNDEFINED != null || x.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY) && (x.UNDEFINED == OtherValues.PLUSINFINITY || x.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(OtherValues.UNDEFINED);
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(this.UNDEFINED);
                }
                if ((x.UNDEFINED == OtherValues.PLUSINFINITY || x.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(0);
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            if (x.isZero())
            {
                if (this.isZero())
                {
                    return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
                }
                return new Complex(OtherValues.PLUSINFINITY); //Operation not supported with undefined values, return an undefined number
            }
            return this.mult(x.conjugate()).div((x.REAL*x.REAL)+(x.IMAGINARY*x.IMAGINARY));
        }
        /**
         * Returns the complex conjugate
         * @return 
         */
        public Complex conjugate()
        {
            if (this.UNDEFINED != null )
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(this.REAL, -this.IMAGINARY);
        }
        /**
         * Returns the absolute value
         * @return 
         */
        public double abs()
        {
            if (this.UNDEFINED != null)
            {
                return Double.NaN;
            }
            return Math.sqrt((this.REAL*this.REAL)+(this.IMAGINARY*this.IMAGINARY));
        } 
        /**
         * Used to multiply the complex number but some real only component
         * @param x
         * @return 
         */
        public Complex mult(double x)
        {
            if (this.UNDEFINED  != null || !Double.isFinite(x))
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x == Double.NaN)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x == Double.POSITIVE_INFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x == Double.NEGATIVE_INFINITY))
                {
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY && x == Double.NEGATIVE_INFINITY) || (this.UNDEFINED == OtherValues.MINUSINFINITY && x == Double.POSITIVE_INFINITY))
                {
                    return new Complex(OtherValues.MINUSINFINITY);
                }
                if (this.UNDEFINED == OtherValues.PLUSINFINITY || x == Double.POSITIVE_INFINITY)
                {
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if (this.UNDEFINED == OtherValues.MINUSINFINITY || x == Double.NEGATIVE_INFINITY)
                {
                    return new Complex(OtherValues.MINUSINFINITY);
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(this.REAL * x, this.IMAGINARY * x);
        }
        /**
         * Used to divide the complex number by some real component
         * @param x
         * @return 
         */
        public Complex div(double x)
        {
            if (this.UNDEFINED  != null || !Double.isFinite(x))
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || x == Double.NaN)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY) && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY))
                {
                    return new Complex(OtherValues.UNDEFINED);
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(this.UNDEFINED);
                }
                if ((x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY))
                {
                    return new Complex(0);
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            if (x <= TRANSCENDENTALINACCURACY) //If zero
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(this.REAL / x, this.IMAGINARY / x);
        }
        /**
         * Used to calculate (A+Bi)^(C+Di)
         * @param power
         * @return Returns the first number raised to the power of the parameter power
         */
        public Complex power(Complex power)
        {
            if (this.UNDEFINED != null || power.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || power.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY) && power.UNDEFINED == OtherValues.PLUSINFINITY)
                {
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if (power.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    return new Complex(0); //A^-inf = 0
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            if (this.isZero() && power.isLessThanEqualTo(new Complex(0)))
            {
                if (power.isZero())
                {
                    return new Complex(OtherValues.UNDEFINED); //The answer is undefined
                }
                return new Complex(OtherValues.PLUSINFINITY); //The answer is undefined
            }
            double real = Math.pow(this.abs(), power.REAL) * Math.pow(Math.E, -power.IMAGINARY * this.arg()) * Math.cos((power.REAL * this.arg()) + (power.IMAGINARY * Math.log(this.abs())));
            double imaginary = Math.pow(this.abs(), power.REAL) * Math.pow(Math.E, -power.IMAGINARY * this.arg()) * Math.sin((power.REAL * this.arg()) + (power.IMAGINARY * Math.log(this.abs())));
            return new Complex(real, imaginary);
            
        }
        /**
         * 
         * @param base The complex base of the logarithm
         * @return 
         */
        public Complex log(Complex base)
        {
            if (this.UNDEFINED  != null || base.UNDEFINED != null)
            {
                if (this.UNDEFINED == OtherValues.UNDEFINED || base.UNDEFINED == OtherValues.UNDEFINED)
                {
                    return new Complex(OtherValues.UNDEFINED); //Undefined values are still undefined
                }
                if ((this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY) || (base.UNDEFINED == OtherValues.PLUSINFINITY || base.UNDEFINED == OtherValues.MINUSINFINITY))
                {
                    return new Complex(OtherValues.UNDEFINED);
                }
                if (this.UNDEFINED == OtherValues.PLUSINFINITY || this.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    return new Complex(OtherValues.PLUSINFINITY);
                }
                if (base.UNDEFINED == OtherValues.PLUSINFINITY || base.UNDEFINED == OtherValues.MINUSINFINITY)
                {
                    return new Complex(0); //log base inf (x) = 0
                }
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(Math.log(this.abs()), this.arg()).div(new Complex(Math.log(base.abs()), base.arg()));
        }
        /**
         * Returns the argument of the complex number
         * @return Returns the argument of the number
         */
        public double arg()
        {
            if (this.UNDEFINED != null)
            {
                return Double.NaN;
            }
            return Math.atan2(IMAGINARY, REAL);
        }
        /*
        TRIG FUNCTIONS
        */
        /**
         * Returns the sin of this complex value
         * @return Returns sin(A+Bi)
         */
        public Complex sin()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            double real = (Math.sin(this.REAL) / 2.0) * (Math.pow(Math.E, -this.IMAGINARY) + Math.pow(Math.E, this.IMAGINARY));
            double imaginary = -(Math.cos(this.REAL) / 2.0) * (Math.pow(Math.E, -this.IMAGINARY) - Math.pow(Math.E, this.IMAGINARY));
            return new Complex(real, imaginary);
        }
        /**
         * Returns the cosine of this complex value
         * @return Returns cos(A+Bi)
         */
        public Complex cos()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            double real = (Math.cos(this.REAL) / 2.0) * (Math.pow(Math.E, -this.IMAGINARY) + Math.pow(Math.E, this.IMAGINARY));
            double imaginary = (Math.sin(this.REAL) / 2.0) * (Math.pow(Math.E, -this.IMAGINARY) - Math.pow(Math.E, this.IMAGINARY));
            return new Complex(real, imaginary);
        }
        /**
         * Returns the tangent of this complex value
         * @return Returns tan(A+Bi)
         */
        public Complex tan()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return this.sin().div(this.cos()); //tan(x) = sin(x) / cos(x)
        }
        /**
         * 
         * @return Returns the cosecant
         */
        public Complex cosec()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.sin()); //cosec x = 1/sin x
        }
        /**
         * 
         * @return Returns the secant
         */
        public Complex sec()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.cos()); //sec x = 1/cos x
        }
        /**
         * 
         * @return Returns the cotangent
         */
        public Complex cot()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.tan());
        }
        /*
        INVERSE TRIG FUNCTIONS
        */
        /**
         * Calculates the arc sine of the complex angle.
         * @return Returns a complex value which is a solution to the arc sine/inverse sine
         */
        public Complex arcsin()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(0, -1).mult(new Complex(1).sub(this.power(new Complex(2))).power(new Complex(0.5)).add(new Complex(0, 1).mult(this)).log(Complex.E()));

            /*
            //Formula uses A and B variables to simplify repeated parts

            double A = (Math.sqrt(Math.pow(1+this.REAL, 2) + Math.pow(this.IMAGINARY, 2)) - Math.sqrt(Math.pow(1-this.REAL, 2) + Math.pow(this.IMAGINARY, 2))) / 2.0;
            double B = (Math.sqrt(Math.pow(1+this.REAL, 2) + Math.pow(this.IMAGINARY, 2)) + Math.sqrt(Math.pow(1-this.REAL, 2) + Math.pow(this.IMAGINARY, 2))) / 2.0;
            double real = Math.asin(A);
            //According to wolfram alpha only one of the following is true so find out which one
            double imaginaryPositive = Math.log(B + Math.sqrt(Math.pow(B, 2)-1));
            double imaginaryNegative = Math.log(B - Math.sqrt(Math.pow(B, 2)-1));
            Complex comp1 = new Complex(real, imaginaryPositive);
            Complex comp2 = new Complex(real, imaginaryNegative);
            if (comp1.sin().sub(this).abs() < comp2.sin().sub(this).abs())
            { //Find out which solution is NOT the conjugate to the answer
                return comp1;
            }
            else
            {
                return comp2;
            }
            */
        }
        /**
         * Calculates the arc cosine of the complex angle.
         * @return Returns a complex value which is a solution to the arc cosine/inverse cosine
         */
        public Complex arccos()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return Complex.PI().div(new Complex(2)).sub(this.arcsin());
            /*
            //Formula uses A and B variables to simplify repeated parts
            double A = (Math.sqrt(Math.pow(1+this.REAL, 2) + Math.pow(this.IMAGINARY, 2)) - Math.sqrt(Math.pow(1-this.REAL, 2) + Math.pow(this.IMAGINARY, 2))) / 2.0;
            double B = (Math.sqrt(Math.pow(1+this.REAL, 2) + Math.pow(this.IMAGINARY, 2)) + Math.sqrt(Math.pow(1-this.REAL, 2) + Math.pow(this.IMAGINARY, 2))) / 2.0;
            double real = Math.acos(A);
            //According to wolfram alpha only one of the following is true so find out which one
            double imaginaryPositive = -Math.log(B + Math.sqrt(Math.pow(B, 2)-1));
            double imaginaryNegative = -Math.log(B - Math.sqrt(Math.pow(B, 2)-1));
            Complex comp1 = new Complex(real, imaginaryPositive);
            Complex comp2 = new Complex(real, imaginaryNegative);
            if (comp1.cos().sub(this).abs() < comp2.cos().sub(this).abs())
            { //Find out which solution is NOT the conjugate to the answer
                return comp1;
            }
            else
            {
                return comp2;
            }
            */
        }
        /**
         * Calculates the arc tangent of the complex angle.
         * @return Returns a complex value which is a solution to the arc tangent/inverse tangent
         */
        public Complex arctan()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(0, -0.5).mult(new Complex(0, 1).sub(this).div(new Complex(0, 1).add(this)).log(Complex.E()));
            /*
            double real = 0.5 * Math.atan2(2.0 * this.REAL, 1 - Math.pow(this.REAL, 2) - Math.pow(this.IMAGINARY, 2));
            double complex = 0.25 * Math.log(Math.pow(this.REAL, 2) + Math.pow(this.IMAGINARY + 1.0, 2) / Math.pow(this.REAL, 2) + Math.pow(this.IMAGINARY - 1.0, 2));
            return new Complex(real, complex);
            */
        }
        /*
        HYPERBOLIC FUNCTIONS
        */
        /**
         * Find the hyperbolic sine (sinh) of my number
         * @return 
         */
        public Complex sinh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return Complex.E().power(this).sub(Complex.E().power(new Complex().sub(this))).div(2);
        }
        /**
         * Find the hyperbolic cosine (cosh) of my number
         * @return 
         */
        public Complex cosh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return Complex.E().power(this).add(Complex.E().power(new Complex().sub(this))).div(2);
        }
        /**
         * Find the hyperbolic tangent (tanh) of my number
         * @return 
         */
        public Complex tanh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return this.sinh().div(this.cosh()); //tanh x = sinh x / cosh x
        }
        /**
         * 
         * @return 
         */
        public Complex cosech()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.sinh());
        }
        /**
         * 
         * @return 
         */
        public Complex sech()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.cosh());
        }
        /**
         * 
         * @return 
         */
        public Complex coth()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(1).div(this.tanh());
        }
        /**
         * Find the inverse hyperbolic sine (asine) of my number
         * @return 
         */
        public Complex arcsinh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return this.add(this.power(new Complex(2)).add(new Complex(1)).power(new Complex(0.5))).log(Complex.E()); //Formula for inverse sinh
        }
        /**
         * Find the inverse hyperbolic cosine (acosh) of my number
         * @return 
         */
        public Complex arccosh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return this.add(this.sub(new Complex(1)).power(new Complex(0.5)).mult(this.add(new Complex(1)).power(new Complex(0.5)))).log(Complex.E());
        }
        /**
         * Find the inverse hyperbolic tangent (atanh) of my number
         * @return 
         */
        public Complex arctanh()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            return new Complex(0.5).mult(new Complex(1).add(this).log(Complex.E()).sub((new Complex(1).sub(this)).log(Complex.E())));
        }
        /**
         * Factorial Function
         * ONLY WORKS WITH WHOLE NUMBNERS
         * @return 
         */
        public Complex factorial()
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            if (this.isReal())
            { //If Integer Only
                /*
                Recursive function for factorials
                */
                /*
                if (this.isZero())
                {
                    return new Complex(1);
                }
                else
                {
                    Complex x = this.round();
                    return x.mult(x.sub(new Complex(1)).factorial());
                }
                */
                return this.add(new Complex(1)).gammaFunction(); //Compute using the gamma function
            }
            else
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
        }
        /**
         * Rounds to the nearest integer values
         * @return 
         */
        public Complex round()
        {
            return round(0);
        }
        /**
         * Rounds to the nearest integer values
         * @param places The number of decimal places to round to 0= round to the nearest integer
         * @return 
         */
        public Complex round(int places)
        {
            double scale = Math.pow(10, places);
            return new Complex(Math.round(this.REAL * scale) / scale, Math.round(this.IMAGINARY * scale) / scale);
        }
        /**
         * Returns the MOD of a number
         * NOTE: Currently only works with real values
         * @param divisor The divisor
         * @return 
         */
        public Complex mod(Complex divisor)
        {
            if (this.UNDEFINED != null)
            {
                return new Complex(OtherValues.UNDEFINED); //Operation not supported with undefined values, return an undefined number
            }
            if (this.isReal() && divisor.isReal())
            { //If real only
                return new Complex(this.REAL % divisor.REAL);
            }
            else
            {
                throw new UnsupportedOperationException("Operation: MOD, For Complex Values Not Supported Yet!"); //Operation not supported yet
            }
        }
        /**
         * Returns the result of this value with the combination operator with r
         * Result = nCr
         * @param r
         * @return Returns result = nCr
         */
        public Complex Combination(Complex r)
        {
            if (this.isNaturalNumber() && r.isNaturalNumber())
            {
                if (r.isLessThanEqualTo(this))
                {
                    return this.factorial().div(r.factorial().mult(this.sub(r).factorial())); //nCr = n! / (r!(n-r)!)
                }
                else
                {
                    throw new UnsupportedOperationException("r cannot be greater than n"); //Operation not supported
                }
            }
            else
            {
                throw new UnsupportedOperationException("nCr does not work with non Integer Values"); //Operation not supported
            }
        }
        /**
         * Computes the gamma function
         * @return 
         */
        public Complex gammaFunction()
        {
            if (this.isReal())
            {
                return new Complex(realGammaFunction(this.real()));
            }
            else
            {
                throw new UnsupportedOperationException("Gamma function does not work with non reals yet"); //Operation not supported
            }
	}
        public static double realGammaFunction(double x)
        {
            /*
            CREDIT TO
            https://rossetacode.org/wiki/Gamma_function#Java
            FOR ALGORITHM HERE
            Sadly I don't understand this bit
            */
            double[] p = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
                            771.32342877765313, -176.61502916214059, 12.507343278686905,
                            -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
            int g = 7;
            if(x < 0.5) return Math.PI / (Math.sin(Math.PI * x)*realGammaFunction(1-x));

            x -= 1;
            double a = p[0];
            double t = x+g+0.5;
            for(int i = 1; i < p.length; i++){
                    a += p[i]/(x+i);
            }

            return Math.sqrt(2*Math.PI)*Math.pow(t, x+0.5)*Math.exp(-t)*a;
        }
        /**
         * Returns the real component
         * @return Returns the real component
         */
        public double real()
        {
            return this.REAL;
        }
        /**
         * Returns the imaginary component
         * @return Returns the imaginary component
         */
        public double im()
        {
            return this.IMAGINARY;
        }
        /**
         * The constant E
         * @return Returns the constant E
         */
        public static Complex E()
        {
            return new Complex (Math.E);
        }
        /**
         * The constant Pi
         * @return Returns Pi
         */
        public static Complex PI()
        {
            return new Complex (Math.PI);
        }
        /**
         * The Golden ratio
         * @return Returns the constant Phi
         */
        public static Complex PHI()
        {
            return new Complex ((Math.sqrt(5.0) + 1.0) / 2.0);
        }
        /**
         * The imaginary constant I
         * @return returns the imaginary constant I
         */
        public static Complex I()
        {
            return new Complex (0, 1);
        }
        /**
         * Used to test if this complex value is on the real line (is a real number/complex value = 0)
         * @return Returns true if this is a real value
         */
        public boolean isReal()
        {
            return Math.abs(this.IMAGINARY) < TRANSCENDENTALINACCURACY && UNDEFINED == null ; //If the imaginary component is bassically 0
        }
        /**
         * Used to test if this complex value is on the imaginary line (is a imaginary component only/real value = 0)
         * @return Returns true if this number is complex only
         */
        public boolean isImaginary()
        {
            return Math.abs(this.REAL) < TRANSCENDENTALINACCURACY && UNDEFINED == null; //If the real component is bassically 0
        }
        /**
         * Returns true if this complex number is a real only integer (Whole number)
         * @return Returns true if this complex number is a real only integer (Whole number)
         */
        public boolean isInteger()
        {
            return isReal() && (this.REAL % 1.0  < TRANSCENDENTALINACCURACY);
        }
        
        /**
         * Returns true if this complex number is a real only, positive integer (Whole number) this is know as a natural number
         * @return Returns true if this complex number is a real only, positive integer (Whole number) this is know as a natural number
         */
        public boolean isNaturalNumber()
        {
            return isInteger() && isGreaterThanEqualTo(new Complex());
        }
        /**
         * Returns true if (approximately) zero
         * @return 
         */
        public boolean isZero()
        {
            return (Math.abs(this.REAL) < TRANSCENDENTALINACCURACY) && (Math.abs(this.IMAGINARY) < TRANSCENDENTALINACCURACY && UNDEFINED == null);
        }
    
        /**
         * Returns true if (approximately) zero
         * This function you can specify the accuracy you desire
         * @param accuracy The accuracy of the comparison, useful for approximations or graph drawing
         * @return 
         */
        public boolean isZero(double accuracy)
        {
            return (Math.abs(this.REAL) < accuracy) && (Math.abs(this.IMAGINARY) < accuracy && UNDEFINED == null);
        }
        /**
         * 
         * @return Returns true is result is undefined
         */
        public boolean isUndefined()
        {
            return this.UNDEFINED == OtherValues.UNDEFINED;
        }
        /**
         * 
         * @return Returns true if value is +infinity
         */
        public boolean isPositiveInfinity()
        {
            return this.UNDEFINED == OtherValues.PLUSINFINITY;
        }
        /**
         * 
         * @return Returns true if value is -infinity
         */
        public boolean isNegativeInfinity()
        {
            return this.UNDEFINED == OtherValues.MINUSINFINITY;
        }
        /**
         * 
         * @return Returns true if infinity either plus or minus
         */
        public boolean isInfinite()
        {
            return this.isPositiveInfinity() || this.isNegativeInfinity();
        }
        /**
         * 
         * @return Returns true if finite and not undefined
         */
        public boolean isFinite()
        {
            return this.UNDEFINED == null;
        }
        /**
         * Returns true if the two values are equal
         * @param x The number to test against
         * @return 
         */
        public boolean equals(Complex x)
        {
            return this.sub(x).isZero();
        }
        /**
         * Returns true if the two values are equal
         * @param x The number to test against
         * @param accuracy The accuracy required for the equality to be true
         * @return 
         */
        public boolean equals(Complex x, double accuracy)
        {
            return this.sub(x).isZero(accuracy);
        }
        public boolean isGreaterThan(Complex x)
        {
            return this.REAL > x.REAL;
        }
        
        public boolean isGreaterThanEqualTo(Complex x)
        {
            return this.REAL >= x.REAL;
        }
        public boolean isLessThan(Complex x)
        {
            return this.REAL < x.REAL;
        }
        
        public boolean isLessThanEqualTo(Complex x)
        {
            return this.REAL <= x.REAL;
        }
        /**
         * Returns true if this number is pi
         * @return Returns true if this number is pi
         */
        public boolean isPi()
        {
            return this.equals(Complex.PI());
        }
        /**
         * Returns true if this number is e
         * @return Returns true if this number is e
         */
        public boolean isE()
        {
            return this.equals(Complex.E());
        }
        /**
         * Returns true if this number is phi
         * @return Returns true if this number is phi
         */
        public boolean isPHI()
        {
            return this.equals(Complex.PHI());
        }
        /**
         * Used to represent the complex number in A + Bi form
         * @return Returns a string in the form A + Bi
         */
        public String write()
        {
            return write((int) BASEDISPLAYACCURACY); //Writes out to a certain number of digits
        }
        

        /**
         * Used to represent the complex number in A + Bi form
         * @param maxNumbers
         * @return Returns a string in the form A + Bi
         */
        public String write(int maxNumbers)
        {
            /*
            SET VARIABLE LENGTH TO AN APPROPRIATE LEVEL
            */
            Complex rounded = this.round(maxNumbers);
            
            if (this.UNDEFINED == OtherValues.UNDEFINED)
            {
                return "ERROR";
            }
            if (this.UNDEFINED == OtherValues.PLUSINFINITY)
            {
                return "+∞";
            }
            if (this.UNDEFINED == OtherValues.MINUSINFINITY)
            {
                return "-∞";
            }
            else if (rounded.REAL != 0 && rounded.IMAGINARY != 0)
            {
                if (rounded.IMAGINARY >= 0)
                {
                    return rounded.REAL + " + " + rounded.IMAGINARY + "i";
                }
                else
                {
                    return rounded.REAL + " " + rounded.IMAGINARY + "i";
                }
            }
            else if (rounded.REAL != 0 && rounded.IMAGINARY == 0)
            {
                
                return rounded.REAL + "";
            }
            else if (rounded.REAL == 0 && rounded.IMAGINARY != 0)
            {
                return rounded.IMAGINARY + "i";
            }
            else
            {
                return "0";
            }
        }
    }
    /*
    public static class StoredData
    {
        public static class StoredConstants
        {
            public final Complex VALUE;
            public final String NAME;
            public final String DESCRIPTION;
            public final Category CATEGORY;
            
            public StoredConstants(Complex VALUE, String name, String description, Category category)
            {
                this.VALUE = VALUE;
                this.NAME = name;
                this.DESCRIPTION = description;
                this.CATEGORY = category;
            }
            
            public String display()
            {
                return "--- " + NAME + " ---\n" + VALUE.write() + "\n" + DESCRIPTION + "\nCategory: " + CATEGORY; 
            }
        }
        public static class StoredEquations
        {
            public final Equation E;
            public final String NAME;
            public final String DESCRIPTION;
            public final Category CATEGORY;
            
            public StoredEquations(Equation e, String name, String description, Category category)
            {
                this.E = e;
                this.NAME = name;
                this.DESCRIPTION = description;
                this.CATEGORY = category;
            }
            
            public String display()
            {
                return "--- " + NAME + " ---\n" + E.write(NotationType.INFIX) + "\n" + DESCRIPTION + "\nCategory: " + CATEGORY; 
            }
        }
        
        private static final ArrayList<StoredEquations>equations;
        private static final ArrayList<StoredConstants>constants;
        public enum Category
        {
            MECHANICS,
            IDENTITIES,
            PURE
        }
        
        static
        {
            equations = new ArrayList<>();
            constants = new ArrayList<>();
        }
        private static void loadEquations()
        {
            equations.add(
                    new StoredEquations
            (
                    new Equation(stringToObject("F", NotationType.PREFIX), Equation.EquationType.EQ, stringToObject("* m a", NotationType.PREFIX)), 
                    "Newton's Second Law", 
                    "Mass x Acceleration is proportional to force",
                    Category.MECHANICS
            ));
            equations.add(
                    new StoredEquations
            (
                    new Equation(stringToObject("F", NotationType.PREFIX), Equation.EquationType.EQ, stringToObject("/ * G * M m ^ r 2", NotationType.PREFIX)), 
                    "Newton's Gravity Formula", 
                    "Formula for Gravity",
                    Category.MECHANICS
            ));
            equations.add(
                    new StoredEquations
            (
                    new Equation(stringToObject("x", NotationType.PREFIX), Equation.EquationType.EQ, stringToObject("/ + * -1 b ^ - ^ b 2 * 4 * a c 0.5 * 2 a", NotationType.PREFIX)), 
                    "Quadratic Formula", 
                    "Solves Quadratic Equations",
                    Category.PURE
            ));
            equations.add(
                    new StoredEquations
            (
                    new Equation(stringToObject("E", NotationType.PREFIX), Equation.EquationType.EQ, stringToObject("* 0.5 * m ^ v 2", NotationType.PREFIX)), 
                    "Kinetic Energy", 
                    "Works out kinetic enrgy of an object",
                    Category.MECHANICS
            ));
            
        }
        
        public static String displayAllFormulae()
        {
            int i;
            String result = "";
            for (i = 0; i < equations.size(); i++)
            {
                result += displayFormulae(i) + "\n\n";
            }
            return result;
        }
        
        public static String displayFormulae(int ID)
        {
            return equations.get(ID).display();
        }

    }
    */
    /**
     * used to substitute in values into variables
     */
    public static class Substitution
    {
        public Substitution(String name, double value)
        {
            this.NAME = name;
            this.VALUE = new Complex(value);
        }
        
        public Substitution(String name, Complex value)
        {
            this.NAME = name;
            this.VALUE = value;
        }
        public final String NAME;
        public final Complex VALUE;
    }
    /**
     * Used to store a generic object which can either be a variable, value or another operation 
     * NOTE: under normal mathematical definitions a term cannot itself contain operations such as add, this is not the case with this object (I just ran out words sorry)
     */
    public static abstract class Term
    {
        /**
         * Writes out to function
         * @param notation
         * @return Returns a string representation of the function  in INFIX notation
         */
        public abstract String write(NotationType notation);
        /**
         * this functions returns the specific differential e.g. d/dVARIABLE(EXPRESSION)
         * 
         * NOTE: This version of the function allows you to ignore certain variables (set their differential to 0) 
         * This is useful for calculating the Jacobian matrix
         * 
         * 
         * @param variable The variable to differentiate with respect to
         * @param resultingVariable The variable the function is equal to e.g y in example y = f(x) where y becomes dy/dx
         * @param variablesIgnoring The variables to set to 0
         * @return Returns the differential of the expression
         */
        public abstract Term differentiate(String variable, String resultingVariable, ArrayList<Substitution> variablesIgnoring);
        /**
         * Returns true If this expression explicitly contains no variables
         * e.g
         * 5+(8*3) => TRUE
         * 0*x =>FALSE (implicitly contains no variables)
         * x-x =>FALSE (implicitly contains no variables)
         * 1-x =>FALSE
         * @return Returns true If this expression explicitly contains no variables
         */
        public abstract boolean explicitlyContainsNoVariables();
        /**
         * Can perform basic algebra simplification
         * @return Returns a simplified and equivalent expression
         */
        public abstract Term Simplify();
        /**
         * Finding the value of an expression
         * @param sub Any variable values to substitute in
         * @return Returns the value of an expression
         */
        public abstract Complex valueOf(ArrayList<Substitution> sub);
        /**
         * Finding the value of an expression
         * @return Returns the value of an expression
         */
        public abstract Complex valueOf();
        /**
         * Finding the value of an expression
         * @param sub Any variable values to substitute in
         * @return Returns the value of an expression
         */
        public abstract Matrix matrixValueOf(ArrayList<Substitution> sub);
        /**
         * Finding the value of an expression
         * @return Returns the value of an expression
         */
        public abstract Matrix matrixValueOf();
        /**
         * Makes a deep copy of this Term
         * @return Returns an identical but deep copied object
         */
        public abstract Term copy();
        
        /**
         * Finds the roots of an expression which satisfy f(x)=0 where the expression if f(x)
         * @param variable The variable for x
         * @return Returns a list of approximate roots note this may not be every root
         */
        public abstract Complex[] findMyRoots(String variable);
        
        public abstract String type();
        
        /**
         * This function is used to calculate the algebraic Taylor expansion
         * @param a The starting position of the Taylor expansion (For Maclurin Series use a=0)
         * @param variableExpanding The exact name of the variable expanding (NOTE: trying to expand an expression with more than one variable will throw an error)
         * @param maxPower The highest power value of the Taylor expansion
         * @return 
         */
        public abstract Term taylorSeries(Complex a, String variableExpanding, int maxPower);
    }
    /**
     * Stores a single variable in an expression
     */
    public static class Variable extends Term
    {
        //USed for storing variables
        public Variable(String name)
        {
            int i;
            for (i = 0; i < name.length()-1; i++)
            {
                switch (name.substring(i, i+1))
                {
                    case "+":
                    case "-":
                    case "*":
                    case "%":
                        throw new UnsupportedOperationException("Variables cannot contain these characters"); //Varibales can't contain these characters
                    default:
                        break;
                }
            }
            this.NAME = name;
        }
        public final String NAME;
        /**
         * Writes out to function
         * @param notation
         * @return Returns a string representation of the function  in INFIX notation
         */
        @Override
        public String write(NotationType notation)
        {
            return NAME;
        }
        /**
         * this functions returns the specific differential e.g. d/dVARIABLE(EXPRESSION) where 
         * @param variable The variable to differentiate with respect to
         * @param resultingVariable The variable the function is equal to e.g y in example y = f(x) where y becomes dy/dx
         * @return Returns the differential of the expression
         */
        @Override
        public Term differentiate(String variable, String resultingVariable, ArrayList<Substitution> variablesIgnoring)
        {
            if (this.NAME.equals(variable))
            { //If differntiating with respect to my variable
                //Use Power rule
                return new Value(1);
            }
            else if (this.NAME.equals(resultingVariable))
            { //If differntiating with respect to my variable
                //Use Power rule
                int diffValue = getDifferentialCount() + 1;
                if (diffValue == 1)
                {
                    return new Variable("d"+resultingVariable+"/d"+variable);
                }
                else
                {
                    return new Variable("d^"+diffValue+this.getFunctionValueDiff()+"/d"+this.getVaryingVariableDiff()+"^"+diffValue);
                }
            }
            else
            { //If differentiating another variable
                for (Substitution x : variablesIgnoring)
                {
                    if (x.NAME.equals(this.NAME)) //If found a suitable substitution
                    {
                        return new Value(0);
                    }
                }
                //Use other rules
                throw new UnsupportedOperationException("Differentiating different variables (" + NAME + ") NOT supported yet."); //Haven't added any way to solve this yet
            }
        }
        
        private String getVaryingVariableDiff()
        {
            if (this.getDifferentialCount() > 0)
            {
                int i;
                for (i = 1; i < this.NAME.length()-1; i++)
                {
                    if (this.NAME.substring(i-1, i+1).equals("/d"))
                    {
                        break;
                    }
                }
                int i2;
                for (i2 = i; i2 < this.NAME.length(); i2++)
                {
                    if (this.NAME.substring(i2, i2+1).equals("^"))
                    {
                        break;
                    }
                }
                return this.NAME.substring(i+1, i2);
            }
            else
            {
                throw new UnsupportedOperationException("No top value yet");
            }
        }
        private String getFunctionValueDiff()
        {
            if (this.getDifferentialCount() > 0)
            {
                int startingPos;
                if (this.NAME.substring(0, 2).equals("d^") && this.NAME.contains("/d"))
                {
                    startingPos = 2;
                }
                else
                {
                    startingPos = 1;
                }
                int  i;
                for (i = startingPos; i < this.NAME.length(); i++)
                {
                    if (!Character.isDigit(this.NAME.charAt(i)))
                    {
                        break;
                    }
                }
                int i2;
                for (i2 = i; i2 < this.NAME.length(); i2++)
                {
                    if (this.NAME.charAt(i2) == '/')
                    {
                        break;
                    }
                } 
                return this.NAME.substring(i, i2);
            }
            else
            {
                return this.NAME;
            }
        }
        private int getDifferentialCount()
        {
            if (this.NAME.length() >= 4)
            {
                if (this.NAME.substring(0, 2).equals("d^") && this.NAME.contains("/d"))
                {
                    int i;
                    for (i = 2; i < this.NAME.length(); i++)
                    {
                        if (!Character.isDigit(this.NAME.charAt(i)))
                        {
                            break;
                        }
                    } 
                    return Integer.parseInt(this.NAME.substring(2, i));
                }
            }
            if (this.NAME.length() >= 3)
            {
                if (this.NAME.substring(0, 1).equals("d") && this.NAME.contains("/d"))
                {
                    return 1;
                }
            }
            return 0;
        }
        
        /**
         * used to find if this expression contains no variables and has some constant value
         * @return Returns true if no variables are found
         */
        @Override
        public boolean explicitlyContainsNoVariables()
        {
            return false;
        }

        @Override
        public Term Simplify()
        {
            return new Variable(NAME); //A variable is already in it's simplest form
        }
        /**
         * Can find the value of any expression if given the correct variable substitution data
         * @param sub The value of each of the variables
         * @return Returns the value of the expression
         */
        @Override
        public Complex valueOf(ArrayList<Substitution> sub)
        {
            for (Substitution x : sub)
            {
                if (x.NAME.equals(this.NAME)) //If found a suitable substitution
                {
                    return x.VALUE;
                }
            }
            throw new UnsupportedOperationException("No Value found for " + this.NAME);
        }
        
        
        @Override
        public Complex valueOf()
        {
            throw new UnsupportedOperationException("No Value found for " + this.NAME);
        }

        @Override
        public Term copy()
        {
            return new Variable(NAME);
        }
        /**
         * Finds the roots of an expression which satisfy f(x)=0 where the expression if f(x)
         * @param variable The variable for x
         * @return Returns a list of approximate roots note this may not be every root
         */
        @Override
        public Complex[] findMyRoots(String variable)
        {
            return new Complex[0];
        }

        @Override
        public String type()
        {
            return "Variable";
        }
        
        /**
         * This function is used to calculate the algebraic Taylor expansion
         * @param a The starting position of the Taylor expansion (For Maclurin Series use a=0)
         * @param variableExpanding The exact name of the variable expanding (NOTE: trying to expand an expression with more than one variable will throw an error)
         * @param maxPower The highest power value of the Taylor expansion
         * @return 
         */
        @Override
        public Term taylorSeries(Complex a, String variableExpanding, int maxPower)
        {
            if (maxPower >= 1)
            { 
                return new Variable(this.NAME);
            }
            else
            {
                return new Value(0);
            }
        }

        @Override
        public Matrix matrixValueOf(ArrayList<Substitution> sub)
        {
            throw new UnsupportedOperationException("Cannot find the matrix value of a complex variable");
        }

        @Override
        public Matrix matrixValueOf()
        {
            throw new UnsupportedOperationException("No Value found for " + this.NAME);
        }
    }
    /**
     * Stores a single value in an expression
     */
    public static class Value extends Term
    {
        public final Complex VALUE;
        
        public Value(double value)
        {
            this.VALUE = new Complex(value);
        }
        
        public Value(Complex value)
        {
            this.VALUE = value;
        }
        /**
         * Writes out to function
         * @return Returns a string representation of the function  in infix notation
         */
        @Override
        public String write(NotationType notation)
        {
            
            if (VALUE.isE())
            {
                return "e";
            }
            else if (VALUE.isPi())
            {
                return "π";
            }
            else if (VALUE.isPHI())
            {
                return "φ";
            }
            else
            {
                return VALUE.write(); //Write the complex number
            }
        }
        /**
         * this functions returns the specific differential e.g. d/dVARIABLE(EXPRESSION) where 
         * @param variable The variable to differentiate with respect to
         * @param resultingVariable The variable the function is equal to e.g y in example y = f(x) where y becomes dy/dx
         * @return Returns the differential of the expression
         */
        @Override
        public Term differentiate(String variable, String resultingVariable, ArrayList<Substitution> variablesIgnoring)
        { 
            return new Value(0); //Differential of a constant is always 0
        }
        /**
         * used to find if this expression contains no variables and has some constant value
         * @return Returns true if no variables are found
         */
        @Override
        public boolean explicitlyContainsNoVariables()
        {
            return true;
        }
        
        @Override
        public Term Simplify()
        {
            return new Value(VALUE);  //A number is already in it's simplest form
        }
        /**
         * Can find the value of any expression if given the correct variable substitution data
         * @param sub The value of each of the variables
         * @return Returns the value of the expression
         */
        @Override
        public Complex valueOf(ArrayList<Substitution> sub)
        {
            return VALUE;
        }
        
        
        @Override
        public Complex valueOf()
        {
            return VALUE;
        }

        @Override
        public Term copy()
        {
            return new Value(VALUE);
        }
        
        /**
         * Finds the roots of an expression which satisfy f(x)=0 where the expression if f(x)
         * @param variable The variable for x
         * @return Returns a list of approximate roots note this may not be every root
         */
        @Override
        public Complex[] findMyRoots(String variable)
        {
            return new Complex[0];
        }

        @Override
        public String type()
        {
            return "Value";
        }
        
        /**
         * This function is used to calculate the algebraic Taylor expansion
         * @param a The starting position of the Taylor expansion (For Maclurin Series use a=0)
         * @param variableExpanding The exact name of the variable expanding (NOTE: trying to expand an expression with more than one variable will throw an error)
         * @param maxPower The highest power value of the Taylor expansion
         * @return 
         */
        @Override
        public Term taylorSeries(Complex a, String variableExpanding, int maxPower)
        {
            return new Value(this.VALUE);
        }
        
        /**
         * Draws straight to the graph object
         * Graphs the current matrix description of points to
         * @param graph The graph to draw to
         * @param color The colour of the graph
         */
        public void graphTo(Graph graph, Color color)
        {
            Graphics2D g = graph.GRAPH.createGraphics();
            g.setColor(color);      
            g.setStroke(new BasicStroke(1 + (2 * BRUSHWIDTH)));
            int x1 = (int) ((0.0 - graph.XMIN) / graph.getXStep());
            int y1 = graph.getHeightInPixels() - (int) ((0.0 - graph.YMIN) / graph.getYStep());
            int x2 = (int) ((this.VALUE.REAL - graph.XMIN) / graph.getXStep());
            int y2 = graph.getHeightInPixels() - (int) ((this.VALUE.IMAGINARY - graph.YMIN) / graph.getYStep());;
            g.drawLine(x1, y1, x2, y2);
        }

        @Override
        public Matrix matrixValueOf(ArrayList<Substitution> sub)
        {
            throw new UnsupportedOperationException("Cannot find the matrix value of a complex value");
        }

        @Override
        public Matrix matrixValueOf()
        {
            throw new UnsupportedOperationException("Cannot find the matrix value of a complex value");
        }
    }
    /**
     * Stores a single operation in an expression
     */
    public static class Operation extends Term
    {
        public final Term TERM1;
        public final Term TERM2;
        public final Operator OPERATION;

        @Override
        public Term copy()
        {
            return new Operation(TERM1, OPERATION, TERM2);
        }

        @Override
        public String type()
        {
            return "Operation";
        }

        
        
        /**
         * Stores all the different operators
         */
        public enum Operator
        {
            ADD,
            SUB,
            MULT,
            DIV,
            POW,
            SIN,
            COS,
            TAN,
            COSEC,
            SEC,
            COT,
            ASIN,
            ACOS,
            ATAN,
            ACOSEC,
            ASEC,
            ACOT,
            SINH,
            COSH,
            TANH,
            COSECH,
            SECH,
            COTH,
            ASINH,
            ACOSH,
            ATANH,
            ACOSECH,
            ASECH,
            ACOTH,
            LOG,
            ABS, 
            ARG,
            FACTORIAL,
            GAMMA,
            MOD,
            COMB,
            REAL,
            IMG,
            DET,
            INVERSE,
            TRANSPOSE,
            DOT,
            SUM,
            LIMIT,
            DDX,
            INTERGAL
        }

        public Operation(Term term1, Operator operation, Term term2)
        {
            this.TERM1 = term1;
            this.TERM2 = term2;
            this.OPERATION = operation;
        }
        /**
         * Writes out to function
         * @param notation
         * @return Returns a string representation of the function in infix notation
         */
        @Override
        public String write(NotationType notation)
        {
            String operator = ""; //The text version of the operator
            boolean oneArgument = false; //Only one argument
            boolean operatorAtEnd = false; //Put the operator at the end (only applied to infix notation)
            if (null != OPERATION) switch (OPERATION)
            { //Check all different operators
                case ADD:
                    operator = " + "; // A + B
                    break;
                case SUB:
                    operator = " - "; // A - B
                    break;
                case MULT:
                    if (TERM1 instanceof Value && (TERM2 instanceof Variable || TERM2 instanceof Operation))
                    {
                        operator = "";
                    }
                    else
                    {
                        operator = " * ";
                    }
                    break;
                case DIV:
                    operator = " / "; // (A) / (B)
                    break;
                case MOD:
                    operator =  " MOD "; // (A) MOD (B)
                    break;
                case POW:
                    operator = " ^ "; // A^B
                    break;
                case SIN:
                    operator = "SIN ";
                    oneArgument = true;
                    break;
                case COS:
                    operator = "COS ";
                    oneArgument = true;
                    break;
                case TAN:
                    operator = "TAN ";
                    oneArgument = true;
                    break;
                case COSEC:
                    operator = "COSEC ";
                    oneArgument = true;
                    break;
                case SEC:
                    operator = "SEC ";
                    oneArgument = true;
                    break;
                case COT:
                    operator = "COT ";
                    oneArgument = true;
                    break;
                case ASIN:
                    operator = "ASIN ";
                    oneArgument = true;
                    break;
                case ACOS:
                    operator = "ACOS ";
                    oneArgument = true;
                    break;
                case ATAN:
                    operator = "ATAN ";
                    oneArgument = true;
                    break;
                case SINH:
                    operator = "SINH ";
                    oneArgument = true;
                    break;
                case COSH:
                    operator = "COSH ";
                    oneArgument = true;
                    break;
                case TANH:
                    operator = "TANH ";
                    oneArgument = true;
                    break;
                case COSECH:
                    operator = "COSECH ";
                    oneArgument = true;
                    break;
                case SECH:
                    operator = "SECH ";
                    oneArgument = true;
                    break;
                case COTH:
                    operator = "COTH ";
                    oneArgument = true;
                    break;
                case ASINH:
                    operator = "ASINH ";
                    oneArgument = true;
                    break;
                case ACOSH:
                    operator = "ACOSH ";
                    oneArgument = true;
                    break;
                case ATANH:
                    operator = "ATANH ";
                    oneArgument = true;
                    break;
                case LOG:
                    if (TERM2 instanceof Value)
                    {
                        if (TERM2.valueOf(null).equals(Complex.E()))
                        { //if the base is aproximately e
                            operator = "LN ";
                            oneArgument = true;
                        }
                    }
                    else
                    {
                        operator = " LOG ";
                    }
                    break;
                case ABS:
                    operator = "ABS "; // Abs(A)
                    oneArgument = true;
                    break;
                case ARG:
                    operator = "ARG "; // Arg(A)
                    oneArgument = true;
                    break;
                case REAL:
                    operator = "REAL "; // Real(A)
                    oneArgument = true;
                    break;
                case IMG:
                    operator = "IMG "; // Img(A)
                    oneArgument = true;
                    break;
                case FACTORIAL:
                    operator = "!"; // A!
                    oneArgument = true;
                    operatorAtEnd = true;
                    break;
                case GAMMA:
                    operator = "Γ "; // A!
                    oneArgument = true;
                    break;
                case COMB: //A C B
                    operator = " nCr ";
                    break;
                case DET: //Determinant (of a matrix) A
                    operator = "DET ";
                    oneArgument = true;
                    break;
                case INVERSE: //INVERSE (of a matrix) A
                    operator = "INV ";
                    oneArgument = true;
                    break;
                case TRANSPOSE: //TRANSPOSE (of a matrix) A
                    operator = "TRANS ";
                    oneArgument = true;
                    break;
                case DOT: //Dot product (of a vector) A
                    operator = " • ";
                    oneArgument = true;
                    break;
                case LIMIT: //Limit A
                    operator = "LIM ";
                    oneArgument = true;
                    break;
                case DDX: //d/dX ()
                    operator = "D/DX ";
                    oneArgument = true;
                    break;
                case INTERGAL: //INTERGAL ()
                    operator = "INTERGAL ";
                    oneArgument = true;
                    break;
                default:
                    throw new UnsupportedOperationException("Operation: " + OPERATION + ", Not Supported For Displaying Yet!"); //Operation not supported yet
            }
            else
            {
                throw new UnsupportedOperationException("No operation found (OPERATION RETURNED NULL), CHECK EXPRESSION INITIALISATION");
            }
            
            if (null == notation)
            {
                return "";
            }
            else switch (notation)
            {
                case INFIX:
                    //Used to work out if brackets should be drawn
                    String open1 = "";
                    String close1 = "";
                    String open2 = "";
                    String close2 = "";
                    if (TERM1 instanceof Operation)
                    {
                        open1 = "(";
                        close1 = ")";
                    }
                    if (!oneArgument)
                    {
                        if (TERM2 instanceof Operation)
                        {
                            open2 = "(";
                            close2 = ")";
                        }
                    }
                    if (oneArgument)
                    { //If one argument
                        if (operatorAtEnd)
                        { //If the operator is at the end
                            return open1 + TERM1.write(notation) + close1 + operator;
                        }
                        else
                        { //If the operator is not at the end
                            return operator + open1 + TERM1.write(notation) + close1;
                        }
                    }
                    else
                    { //If two arguments
                        return open1 + TERM1.write(notation) + close1 + operator + open2 + TERM2.write(notation) + close2;
                    }
                case PREFIX:
                    if (!oneArgument)
                    { //If two arguments
                        return operator + " " + TERM1.write(notation) + " " + TERM2.write(notation);
                    }
                    else
                    { //If term 2 doesn't exist (one argument)
                        return operator + " " + TERM1.write(notation);
                    }
                case POSTFIX:
                    if (!oneArgument)
                    { //If two arguments
                        return TERM1.write(notation) + " " + TERM2.write(notation) + " " + operator;
                    }
                    else
                    { //If term 2 doesn't exist (one argument)
                        return TERM1.write(notation) + " " + operator;
                    }
                default:
                    return "";
            }
        }
        /**
         * this functions returns the specific differential e.g. d/dVARIABLE(EXPRESSION) where 
         * @param variable The variable to differentiate with respect to
         * @param resultingVariable The variable the function is equal to e.g y in example y = f(x) where y becomes dy/dx
         * @return Returns the differential of the expression
         */
        @Override
        public Term differentiate(String variable, String resultingVariable, ArrayList<Substitution> variablesIgnoring)
        { 
            if (null != OPERATION) switch (OPERATION)
            {
                case ADD: //Differentiate add (not much to do here really)
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.ADD, TERM2.differentiate(variable, resultingVariable, variablesIgnoring)); //d/dx (A + B) = d/dx(A) + d/dx(B)
                case SUB: //Differentiate subtract (not much to do here really)
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.SUB, TERM2.differentiate(variable, resultingVariable, variablesIgnoring)); //d/dx (A - B) = d/dx(A) - d/dx(B)
                case MULT: //Using product rule
                    return new Operation(new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, TERM2), Operator.ADD, new Operation(TERM1, Operator.MULT, TERM2.differentiate(variable, resultingVariable, variablesIgnoring))); //d/dx (A * B) = d/dx(A)B + d/dx(B)A
                case DIV: //Using quotient rule
                    return new Operation(new Operation(new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, TERM2), Operator.SUB, new Operation(TERM1, Operator.MULT, TERM2.differentiate(variable, resultingVariable, variablesIgnoring))), Operator.DIV, new Operation(TERM2, Operator.POW, new Value(2))); //d/dx (A / B) = d/dx(A)B - d/dx(B)A  / B^2
                case POW: //Differentiate powers
                    if (TERM2.explicitlyContainsNoVariables())
                    { //Using chain rule if power is a constant value eg. y = f(g(x))
                        return new Operation(TERM2, Operator.MULT, new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(TERM1, Operator.POW, new Operation(TERM2, Operator.SUB, new Value(1))))); //d/dx (A ^ B) = B(d/dx(A))(A)^(B-1)
                    }
                    else if (TERM1.explicitlyContainsNoVariables())
                    { //Using weird derived rule I seem to have made up
                        return new Operation(TERM2.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Operation(TERM1, Operator.LOG, new Value(Math.E)), Operator.MULT, new Operation(TERM1, Operator.POW, TERM2 )));
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Operation: " + OPERATION + " Not Supported! Powers that are not in the form f(x)^A or A^f(x) where A is a constant are not supported yet");
                    }
                case LOG:
                    if (TERM2.explicitlyContainsNoVariables())
                    { //Differentiate using advance log rule 
                        return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(TERM1, Operator.MULT, new Operation(TERM2, Operator.LOG, new Value(Math.E)))); //d/dx logB f(x) = f'(x) / (f(x)ln B)
                    }
                    else
                    { //Haven't found a good rule for this yet
                        throw new UnsupportedOperationException("Operatoin: " + OPERATION + " Not Supported! Cannot differentiate a log with a non constant base yet");
                    }
                case SIN: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(TERM1, Operator.COS, null)); //d/dx sin x = cos x
                case COS: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(TERM1, Operator.SIN, null))); //d/dx cos x = -sin x
                case TAN: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Operation(TERM1, Operator.SEC, null), Operator.POW, new Value(2))); //d/dx tan x = sec^2 x
                case ASIN:
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Operation(new Value(1), Operator.SUB, new Operation(TERM1, Operator.POW, new Value(2))), Operator.POW, new Value(0.5))); //d/dx asin x = 1/sqrt(1-x^2)
                case ACOS:
                    return new Operation(new Value(-1), Operator.MULT, new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Operation(new Value(1), Operator.SUB, new Operation(TERM1, Operator.POW, new Value(2))), Operator.POW, new Value(0.5)))); //d/dx acos x = -1/sqrt(1-x^2)
                case ATAN:
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Value(1), Operator.ADD, new Operation(TERM1, Operator.POW, new Value(2)))); //d/dx atan x = 1/1+x^2
                case COSEC: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(new Operation(TERM1, Operator.COSEC, null), Operator.MULT, new Operation(TERM1, Operator.COT, null))));
                case SEC: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Operation(TERM1, Operator.SEC, null), Operator.MULT, new Operation(TERM1, Operator.TAN, null)));
                case COT: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(new Operation(TERM1, Operator.COSEC, null), Operator.POW, new Value(2))));
                case SINH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(TERM1, Operator.COSH, null)); //d/dx sinh x = cosh x
                case COSH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(TERM1, Operator.SINH, null)); //d/dx cosh x = sinh x
                case TANH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Operation(TERM1, Operator.SECH, null), Operator.POW, new Value(2))); //d/dx tanh x = sech^2 x
                case COSECH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(new Operation(TERM1, Operator.COTH, null), Operator.MULT, new Operation(TERM1, Operator.COSECH, null)))); //d/dx cosech x = -coth x * cosech x 
                case SECH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(new Operation(TERM1, Operator.TANH, null), Operator.MULT, new Operation(TERM1, Operator.SECH, null))));
                case COTH: //Use chain rule
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.MULT, new Operation(new Value(-1), Operator.MULT, new Operation(new Operation(TERM1, Operator.COSECH, null), Operator.POW, new Value(2))));
                case ASINH:
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Operation(new Value(1), Operator.ADD, new Operation(TERM1, Operator.POW, new Value(2))), Operator.POW, new Value(0.5))); //d/dx asin x = 1/sqrt(1-x^2)
                case ACOSH:
                    return new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Operation(new Operation(TERM1, Operator.POW, new Value(2)), Operator.SUB, new Value(1)), Operator.POW, new Value(0.5))); //d/dx acos x = -1/sqrt(1-x^2)
                case ATANH:
                    return new Operation(new Value(-1), Operator.MULT, new Operation(TERM1.differentiate(variable, resultingVariable, variablesIgnoring), Operator.DIV, new Operation(new Operation(TERM1, Operator.POW, new Value(2)), Operator.ADD, new Value(1)))); //d/dx atan x = 1/1+x^2
                case MOD: //Differentiate mod (not much to do here really)
                    return TERM1.differentiate(variable, resultingVariable, variablesIgnoring); //As far as I can tell (excluding non differentiable points) d/dx f(x) MOD g(x) f'(x) but I'm probably wrong oh well
                default:
                    throw new UnsupportedOperationException("Operatoin: " + OPERATION + " Not Supported yet"); //This operation is not supported yet
            }
            else
            { //If this ever gets thrown something has gone very wrong
                throw new UnsupportedOperationException("No operatoin found (OPERATION RETURNED NULL), CHECK EXPRESSION INITIALISATION");
            }
        }
        /**
         * used to find if this expression contains no variables and has some constant value
         * CASES
         * will correctly identify 5 + 3 * 4 as having no variables
         * However expressions must be simplified and reduced before it could identify 4x-4x or x/x  as containing no variables 
         * @return Returns true if no variables are found
         */
        @Override
        public boolean explicitlyContainsNoVariables()
        {
            boolean result = true;
            if (TERM1 != null)
            {
                result = result && TERM1.explicitlyContainsNoVariables();
            }
            if (TERM2 != null)
            {
                result = result && TERM2.explicitlyContainsNoVariables();
            }
            return result;
        }
        /**
         * Simplifies an expression
         * @return Returns is simplified but equivilent expression
         */
        @Override
        public Term Simplify()
        {
            if (this.explicitlyContainsNoVariables())
            { //If there are no variables why do I need an expression for it
                return new Value(this.valueOf());
            }
            final Term SIMPLIFIEDTERM1;
            final Term SIMPLIFIEDTERM2;
            if (TERM1 != null)
            {
                SIMPLIFIEDTERM1 = TERM1.Simplify();
            }
            else
            {
                SIMPLIFIEDTERM1 = null;
            }
            if (TERM2 != null)
            {
                SIMPLIFIEDTERM2 = TERM2.Simplify();
            }
            else
            {
                SIMPLIFIEDTERM2 = null;
            }
            if (null != OPERATION)
            {
                switch (OPERATION)
                {
                    case MULT:
                        //If I am a multiply operator
                        if (SIMPLIFIEDTERM1 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM1.valueOf();

                            if (termValue.equals(new Complex(1)))
                            { //If it equals 1
                                return SIMPLIFIEDTERM2; //No need to include it so only include the other term
                            }
                            if (termValue.isZero())
                            { //If it equals 0
                                return new Value(0); //No need to include it as 0 x f(x) = 0
                            }
                        }
                        if (SIMPLIFIEDTERM2 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM2.valueOf();

                            if (termValue.equals(new Complex(1)))
                            { //If it equals 1
                                return SIMPLIFIEDTERM1; //No need to include it so only include the other term
                            }
                            if (termValue.isZero())
                            { //If it equals 0
                                return new Value(0); //No need to include it as 0 x f(x) = 0
                            }
                        }
                        TermContainer result = simpifyMultValues(this);
                        if (result.COEFFICIENT.isZero())
                        {
                            return new Value(0); //No need to include it as 0 x f(x) = 0
                        }
                        else if (result.COEFFICIENT.equals(new Complex(1)))
                        {
                            return result.TERM;
                        }
                        return new Operation(new Value(result.COEFFICIENT), Operator.MULT, result.TERM);

                        //break;
                    case DIV:
                        //If I am a divide operator TERM1 / TERM2
                        if (SIMPLIFIEDTERM1 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM1.valueOf();

                            if (termValue.isZero())
                            { //If it equals 0
                                return new Value(0); //No need to include it as 0 / f(x) = 0
                            }
                        }
                        if (SIMPLIFIEDTERM2 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM2.valueOf();

                            if (termValue.equals(new Complex(1)))
                            { //If it equals 1
                                return SIMPLIFIEDTERM1; //No need to include it so only include the other term
                            }
                            if (termValue.isZero())
                            { //If it equals 0
                                if (SIMPLIFIEDTERM1 instanceof Value)
                                {
                                    if (SIMPLIFIEDTERM1.valueOf().isZero())
                                    {
                                        return new Value(new Complex(Complex.OtherValues.UNDEFINED)); //This expression is undefined
                                    }
                                }
                                return new Value(new Complex(Complex.OtherValues.PLUSINFINITY)); //This expression is undefined
                            }
                        }
                        break;
                    case ADD:
                    case SUB:
                        if (SIMPLIFIEDTERM1 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM1.valueOf();
                            if (termValue.isZero())
                            { //If it equals 0
                                if (OPERATION == Operator.ADD)
                                {
                                    return SIMPLIFIEDTERM2; //0 + f(x) = f(x)
                                }
                                else if (OPERATION == Operator.SUB)
                                { //0 - f(x) = -f(x)
                                    return new Operation(new Value(-1), Operator.MULT, TERM2).Simplify(); // 0 - B equals -B not B hence why I multiply by -1
                                }
                            }
                            if (termValue.isLessThan(new Complex()))
                            {
                                if (OPERATION == Operator.ADD)
                                {
                                    return new Operation(SIMPLIFIEDTERM2, Operator.SUB, new Value(termValue.mult(-1))); //(-A) + (B) = B-A
                                }
                                else if (OPERATION == Operator.SUB)
                                {
                                    return new Operation(new Value(-1), Operator.MULT, new Operation(SIMPLIFIEDTERM1, OPERATION, SIMPLIFIEDTERM2));
                                }
                            }
                        }
                        if (SIMPLIFIEDTERM2 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM2.valueOf();
                            if (termValue.isZero())
                            { //If it equals 0
                                return SIMPLIFIEDTERM1;
                            }
                            if (termValue.isLessThan(new Complex()))
                            {
                                if (OPERATION == Operator.ADD)
                                {
                                    return new Operation(SIMPLIFIEDTERM1, Operator.SUB, new Value(termValue.mult(-1)));
                                }
                                else if (OPERATION == Operator.SUB)
                                {
                                    return new Operation(SIMPLIFIEDTERM1, Operator.ADD, new Value(termValue.mult(-1)));
                                }
                            }
                        }
                        if (SIMPLIFIEDTERM2 instanceof Operation)
                        {

                            if (isTermOperationOfType(SIMPLIFIEDTERM2, Operator.MULT))
                            {

                                Term deepTerm1 = ((Operation) SIMPLIFIEDTERM2).TERM1.Simplify();
                                Term deepTerm2 = ((Operation) SIMPLIFIEDTERM2).TERM2.Simplify();
                                //System.out.println("YAY: " + TERM2.Simplify().write(NotationType.INFIX) + " MY TERM1 : " + deepTerm1.write(NotationType.INFIX)  + " MY TERM 2: " + deepTerm2.write(NotationType.INFIX) );
                                if (deepTerm1 instanceof Value)
                                {
                                    Complex deepTerm1Value = deepTerm1.valueOf();
                                    if (deepTerm1Value.equals(new Complex(-1)))
                                    {
                                        if (OPERATION == Operator.ADD)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.SUB, deepTerm2);
                                        }
                                        else if (OPERATION == Operator.SUB)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.ADD, deepTerm2);
                                        }
                                    }
                                    else if (deepTerm1Value.isLessThanEqualTo(new Complex()))
                                    {
                                        if (OPERATION == Operator.ADD)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.SUB, new Operation(new Value(deepTerm1Value.mult(new Complex(-1))), Operator.MULT, deepTerm2));
                                        }
                                        else if (OPERATION == Operator.SUB)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.ADD, new Operation(new Value(deepTerm1Value.mult(new Complex(-1))), Operator.MULT, deepTerm2));
                                        }
                                    }
                                }
                                if (deepTerm2 instanceof Value)
                                {
                                    Complex deepTerm2Value = deepTerm2.valueOf();
                                    if (deepTerm2Value.equals(new Complex(-1)))
                                    {
                                        if (OPERATION == Operator.ADD)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.SUB, deepTerm1);
                                        }
                                        else if (OPERATION == Operator.SUB)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.ADD, deepTerm1);
                                        }
                                    }
                                    else if (deepTerm2Value.isLessThanEqualTo(new Complex()))
                                    {
                                        if (OPERATION == Operator.ADD)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.SUB, new Operation(new Value(deepTerm2Value.mult(new Complex(-1))), Operator.MULT, deepTerm1));
                                        }
                                        else if (OPERATION == Operator.SUB)
                                        {
                                            return new Operation(SIMPLIFIEDTERM1, Operator.ADD, new Operation(new Value(deepTerm2Value.mult(new Complex(-1))), Operator.MULT, deepTerm1));
                                        }
                                    }
                                }
                            }
                        }
                        if (this.OPERATION == Operator.ADD)
                        {
                            //Change to add
                            result = simpifyAddValues(this);
                            if (result.COEFFICIENT.isZero())
                            {
                                return result.TERM;
                            }
                            return new Operation(new Value(result.COEFFICIENT), Operator.ADD, result.TERM);
                        }
                        break;
                    case POW:
                        if (TERM2.Simplify() instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM2.valueOf();
                            if (termValue.isZero())
                            { //If it equals 0
                                if (SIMPLIFIEDTERM1 instanceof Value) //If when simplified the term is just a value
                                {
                                    Complex term1Value = TERM1.Simplify().valueOf();
                                    if (term1Value.isZero())
                                    { //If it equals 0
                                        return new Value(new Complex(Complex.OtherValues.UNDEFINED)); //0^0 is undefined
                                    }
                                }
                                return new Value(1); //f(x)^0 = 1
                            }
                            if (termValue.equals(new Complex(1)))
                            { //If it equals 1
                                return SIMPLIFIEDTERM1; //f(x)^1 = f(x)
                            }
                        }
                        if (SIMPLIFIEDTERM1 instanceof Value) //If when simplified the term is just a value
                        {
                            Complex termValue = SIMPLIFIEDTERM1.valueOf();
                            if (termValue.equals(new Complex(1)))
                            { //If it equals 1
                                return new Value(1); //1^(f(x)) = 1
                            }
                        }
                        if (TERM1 instanceof Operation)
                        { //Power law (x^a)^b = x^(ab)
                            Term newTerm = TERM1;
                            Term resultantTerm = this.TERM2;
                            while (isTermOperationOfType(newTerm, Operator.POW))
                            {
                                resultantTerm = new Operation(resultantTerm, Operator.MULT, ((Operation) newTerm).TERM2);
                                newTerm = ((Operation) newTerm).TERM1;
                            }
                            return new Operation(newTerm, Operator.POW, resultantTerm.Simplify());
                        }
                        break;
                    default:
                        break;
                }
            }
            else
            {
                
            }
            /**
             * If I cannot simplify, simplify my components
             */
            if (TERM1 != null && TERM2 != null)
            {
                return new Operation(SIMPLIFIEDTERM1, OPERATION, SIMPLIFIEDTERM2);
            }
            else if (TERM1 != null)
            {
                return new Operation(SIMPLIFIEDTERM1, OPERATION, null);
            }
            else if (TERM2 != null)
            {
                return new Operation(null, OPERATION, SIMPLIFIEDTERM2);
            }
            else
            {
                return new Operation(null, OPERATION, null);
            }
        }
        /**
         * used to store some results of certain sub simplify functions
         */
        private static class TermContainer
        {
            public final Term TERM;
            public final Complex COEFFICIENT;
            public TermContainer(Term term, Complex coefficient)
            {
                this.TERM = term;
                this.COEFFICIENT = coefficient;
            }
        }
        /**
         * Is used to simplify large chains of multiplying all together and pull out and combine constant terms
         * @param t
         * @return 
         */
        private static TermContainer simpifyMultValues(Term t)
        {

            if (isTermOperationOfType(t, Operator.MULT))
            {
                
                Term simplifiedTerm1 = ((Operation) t).TERM1.Simplify();
                Term simplifiedTerm2 = ((Operation) t).TERM2.Simplify();
                
                //System.out.println("YAY: " + t.write(NotationType.INFIX));
                if (simplifiedTerm1 instanceof Value)
                {
                    TermContainer lowerLayer = simpifyMultValues(simplifiedTerm2);
                    return new TermContainer(lowerLayer.TERM, simplifiedTerm1.valueOf().mult(lowerLayer.COEFFICIENT));
                }
                else if (simplifiedTerm2 instanceof Value)
                {
                    TermContainer lowerLayer = simpifyMultValues(simplifiedTerm1);
                    return new TermContainer(lowerLayer.TERM, simplifiedTerm2.valueOf().mult(lowerLayer.COEFFICIENT));
                }
                else
                {
                    TermContainer lowerLayer1 = simpifyMultValues(simplifiedTerm1);
                    TermContainer lowerLayer2 = simpifyMultValues(simplifiedTerm2);
                    return new TermContainer(new Operation(lowerLayer1.TERM, ((Operation) t).OPERATION, lowerLayer2.TERM), lowerLayer1.COEFFICIENT.mult(lowerLayer2.COEFFICIENT));
                }
            }
            if (t instanceof Operation)
            {    
                if (((Operation) t).TERM1 != null && ((Operation) t).TERM2 != null)
                {
                    return new TermContainer(new Operation(((Operation) t).TERM1.Simplify(), ((Operation) t).OPERATION, ((Operation) t).TERM2.Simplify()), new Complex(1));
                }
                else if (((Operation) t).TERM1 != null)
                {
                    return new TermContainer(new Operation(((Operation) t).TERM1.Simplify(), ((Operation) t).OPERATION, null), new Complex(1));
                }
                else if (((Operation) t).TERM2 != null)
                {
                    return new TermContainer(new Operation(null, ((Operation) t).OPERATION, ((Operation) t).TERM2.Simplify()), new Complex(1));
                }
                else
                {
                    return new TermContainer(new Operation(null, ((Operation) t).OPERATION, null), new Complex(1));
                }
            }
            return new TermContainer(t.Simplify(), new Complex(1));
        }
        
        /**
         * Is used to simplify large chains of adding all together and pull out and combine constant terms
         * @param t
         * @return 
         */
        private static TermContainer simpifyAddValues(Term t)
        {
            if (isTermOperationOfType(t, Operator.ADD))
            {
                
                Term simplifiedTerm1 = ((Operation) t).TERM1.Simplify();
                Term simplifiedTerm2 = ((Operation) t).TERM2.Simplify();
                
                //System.out.println("YAY: " + t.write(NotationType.INFIX));
                if (simplifiedTerm1 instanceof Value)
                {
                    TermContainer lowerLayer = simpifyAddValues(simplifiedTerm2);
                    return new TermContainer(lowerLayer.TERM, simplifiedTerm1.valueOf().add(lowerLayer.COEFFICIENT));
                }
                else if (simplifiedTerm2 instanceof Value)
                {
                    TermContainer lowerLayer = simpifyAddValues(simplifiedTerm1);
                    return new TermContainer(lowerLayer.TERM, simplifiedTerm2.valueOf().add(lowerLayer.COEFFICIENT));
                }
                else
                {
                    TermContainer lowerLayer1 = simpifyAddValues(simplifiedTerm1);
                    TermContainer lowerLayer2 = simpifyAddValues(simplifiedTerm2);
                    return new TermContainer(new Operation(lowerLayer1.TERM, ((Operation) t).OPERATION, lowerLayer2.TERM), lowerLayer1.COEFFICIENT.add(lowerLayer2.COEFFICIENT));
                }
            }
            if (t instanceof Operation)
            {    
                if (((Operation) t).TERM1 != null && ((Operation) t).TERM2 != null)
                {
                    return new TermContainer(new Operation(((Operation) t).TERM1.Simplify(), ((Operation) t).OPERATION, ((Operation) t).TERM2.Simplify()), new Complex(0));
                }
                else if (((Operation) t).TERM1 != null)
                {
                    return new TermContainer(new Operation(((Operation) t).TERM1.Simplify(), ((Operation) t).OPERATION, null), new Complex(0));
                }
                else if (((Operation) t).TERM2 != null)
                {
                    return new TermContainer(new Operation(null, ((Operation) t).OPERATION, ((Operation) t).TERM2.Simplify()), new Complex(0));
                }
                else
                {
                    return new TermContainer(new Operation(null, ((Operation) t).OPERATION, null), new Complex(0));
                }
            }
            return new TermContainer(t.Simplify(), new Complex(0));
        }
        private static boolean isTermOperationOfType(Term t, Operator operation)
        {
            if (t instanceof Operation)
            {
                if (((Operation) t).OPERATION == operation)
                {
                    return true;
                }
            }
            return false;
        }
        /**
         * Can find the value of any expression if given the correct variable substitution data
         * @param sub The value of each of the variables
         * @return Returns the value of the expression
         */
        @Override
        public Complex valueOf(ArrayList<Substitution> sub)
        {
            if (null != OPERATION) switch (OPERATION)
            { //Check all different operators
                case ADD: //Add
                    return TERM1.valueOf(sub).add(TERM2.valueOf(sub)); // Operation = A+B
                case SUB: //Subtract
                    return TERM1.valueOf(sub).sub(TERM2.valueOf(sub)); // Operation = A-B
                case MULT: //Multiply
                    return TERM1.valueOf(sub).mult(TERM2.valueOf(sub)); // Operation = A*B
                case DIV: //Divide
                    return TERM1.valueOf(sub).div(TERM2.valueOf(sub)); // Operation = A/B
                case POW: //Power
                    return TERM1.valueOf(sub).power(TERM2.valueOf(sub)); // Operation = A^B
                case SIN: //Sine
                    return TERM1.valueOf(sub).sin(); // Operation = sin(A)
                case COS: //Cosine
                    return TERM1.valueOf(sub).cos(); // Operation = cos(A)
                case TAN: //Tangent
                    return TERM1.valueOf(sub).tan(); // Operation = tan(A)
                case COSEC:
                    return TERM1.valueOf(sub).cosec();
                case SEC:
                    return TERM1.valueOf(sub).sec();
                case COT:
                    return TERM1.valueOf(sub).cot();
                case ASIN: //Inverse sin
                    return TERM1.valueOf(sub).arcsin(); // Operation = asin(A)
                case ACOS: //Inverse cos
                    return TERM1.valueOf(sub).arccos(); // Operation = acos(A)
                case ATAN: //Inverse tangent
                    return TERM1.valueOf(sub).arctan(); // Operation = atan(A)
                case SINH:
                    return TERM1.valueOf(sub).sinh();
                case COSH:
                    return TERM1.valueOf(sub).cosh();
                case TANH:
                    return TERM1.valueOf(sub).tanh();
                case COSECH:
                    return TERM1.valueOf(sub).cosech();
                case SECH:
                    return TERM1.valueOf(sub).sech();
                case COTH:
                    return TERM1.valueOf(sub).coth();
                case ASINH:
                    return TERM1.valueOf(sub).arcsinh();
                case ACOSH:
                    return TERM1.valueOf(sub).arccosh();
                case ATANH:
                    return TERM1.valueOf(sub).arctanh();
                case LOG: //Logarithm
                    return TERM1.valueOf(sub).log(TERM2.valueOf(sub)); // Operation = logB(A) => ln(A)/ln(B)
                case ABS: //Absulute Value
                    return new Complex(TERM1.valueOf(sub).abs()); 
                case ARG: //The argument of a complex number
                    return new Complex(TERM1.valueOf(sub).arg()); 
                case MOD: //The Modulo arithmetic
                    return TERM1.valueOf(sub).mod(TERM2.valueOf(sub)); 
                case FACTORIAL: //The factorial function
                    return TERM1.valueOf(sub).factorial();
                case GAMMA: //The gamma function
                    return TERM1.valueOf(sub).gammaFunction(); 
                case DET: //The determinant function
                    if (TERM1 instanceof Matrix)
                    {
                        return ((Matrix) TERM1).det().valueOf(sub); //Find the determinant and the substitue in values... why is this getting soo complex lmao
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Cannot perform the determinant on a non matrix"); //Operation not supported 
                    }
                default:
                    throw new UnsupportedOperationException("Operation: " + OPERATION + ", Not Supported Yet!"); //Operation not supported yet
            }
            else
            { //If this ever gets null something has gone very WRONG!
                throw new UnsupportedOperationException("No operatoin found (OPERATION RETURNED NULL), CHECK EXPRESSION INITIALISATION");
            }
        }
        @Override
        public Matrix matrixValueOf(ArrayList<Substitution> sub)
        {
            if (null != OPERATION) switch (OPERATION)
            { //Check all different operators
                case ADD: //Add
                    if (TERM1 instanceof Matrix && TERM2 instanceof Matrix)
                    {
                        return ((Matrix) TERM1.matrixValueOf(sub)).add(((Matrix) TERM2.matrixValueOf(sub)));
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Cannot add a " + TERM1.type() + " to a " + TERM2.type() + " to get a matrix");
                    }
                case SUB: //Subtract
                    if (TERM1 instanceof Matrix && TERM2 instanceof Matrix)
                    {
                        return ((Matrix) TERM1.matrixValueOf(sub)).sub(((Matrix) TERM2.matrixValueOf(sub)));
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Cannot subtract a " + TERM2.type() + " from a " + TERM1.type() + " to get a matrix");
                    }
                case MULT: //Multiply
                    if (TERM1 instanceof Matrix && TERM2 instanceof Matrix)
                    { //If a matrix times a matrix
                        return ((Matrix) TERM1.matrixValueOf(sub)).mult(((Matrix) TERM2.matrixValueOf(sub)));
                    }
                    else if (TERM1 instanceof Matrix)
                    { //If a matrix times a normal value
                        return ((Matrix) TERM1.matrixValueOf(sub)).scale(new Value(TERM2.valueOf(sub)));
                    }
                    else if (TERM2 instanceof Matrix)
                    { //If a matrix times a normal value
                        return ((Matrix) TERM2.matrixValueOf(sub)).scale(new Value(TERM1.valueOf(sub)));
                    }
                    else
                    { //If a complex times a complex
                        throw new UnsupportedOperationException("Cannot multiply a " + TERM1.type() + " by a " + TERM2.type() + " to get a Matrix");
                    }
                case POW: //Power
                    Complex value = TERM2.valueOf(sub);
                    if (TERM1 instanceof Matrix && value.isNaturalNumber())
                    { //If a matrix to the power of a real number
                        return ((Matrix) TERM1).pow((int) value.REAL);
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Matrix powers must be a natural number and first term Must be a matrix to equal a matrix");
                    }
                case INVERSE: //Power
                    if (TERM1 instanceof Matrix)
                    { //If a matrix
                        return ((Matrix) TERM1).inverse();
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Matrix powers must be a natural number");
                    }
                case TRANSPOSE: //Power
                    if (TERM1 instanceof Matrix)
                    { //If a matrix
                        return ((Matrix) TERM1).transpose();
                    }
                    else
                    {
                        throw new UnsupportedOperationException("Matrix powers must be a natural number");
                    }
                default:
                    throw new UnsupportedOperationException("Operation: " + OPERATION + ", Not Supported Yet!"); //Operation not supported yet
            }
            else
            { //If this ever gets null something has gone very WRONG!
                throw new UnsupportedOperationException("No operatoin found (OPERATION RETURNED NULL), CHECK EXPRESSION INITIALISATION");
            }
        }
        /**
         * This value of assumes there are no variables in the equation
         * Can find the value of any expression if given the correct variable substitution data
         * @return Returns the value of the expression
         */
        @Override
        public Complex valueOf()
        {
            ArrayList<Substitution> sub = new ArrayList<>();
            return this.valueOf(sub);
        }
        /**
         * This value of assumes there are no variables in the equation
         * Can find the value of any expression if given the correct variable substitution data
         * @return Returns the value of the expression
         */
        @Override
        public Matrix matrixValueOf()
        {        
            ArrayList<Substitution> sub = new ArrayList<>();
            return this.matrixValueOf(sub);
        }
        /**
         * Finds the roots of an expression which satisfy f(x)=0 where the expression if f(x)
         * @param variable The variable for x
         * @return Returns a list of approximate roots note this may not be every root
         */
        @Override
        public Complex[] findMyRoots(String variable)
        {
            
            final Term F = this; //The function
            final Term DF = F.differentiate(variable, "f("+variable+")", new ArrayList<>()); //The differential of the function

            ArrayList<Complex>roots = new ArrayList<>();
            
            double real;
            double img;

            for (real = REALMIN; real <= REALMAX; real += REALSTEP)
            {
                for (img = IMGMIN; img <= IMGMAX; img += IMGSTEP)
                {
                    Complex result = newtonsMethod(new Complex(real, img), F, DF, variable);
                    if (result.isFinite())
                    {
                        boolean notInList = true;
                        for (Complex x : roots)
                        {
                            if (x.equals(result, ROOTSEQUALACCURACY))
                            {
                                notInList = false;
                            }
                        }
                        if (notInList)
                        {
                            roots.add(result);
                        }
                    }
                }
            }
            
            int i;
            
            Complex[] resultRoots = new Complex[roots.size()];
            for (i = 0; i < roots.size(); i++)
            {
                resultRoots[i] = roots.get(i).div(NEWTONSMETHODACCURACY).round().mult(NEWTONSMETHODACCURACY);
            }
            return resultRoots;
        }
        /**
         * Uses the Newton Raphson formula to approximate roots 
         * @param x the initial variable
         * @param F The function
         * @param DF The differential of the function
         * @param variable The variable representing x
         * @return Returns a possible root if found from this starting position
         */
        private static Complex newtonsMethod(Complex x, final Term F, final Term DF, String variable)
        {
            int i;
            for (i = 0; i < ITERATIONCOUNT; i++)
            {
                ArrayList<Substitution> sub = new ArrayList<>();
                sub.add(new Substitution(variable, x));
                Complex result = x.sub(F.valueOf(sub).div(DF.valueOf(sub)));
                if (result.equals(x, NEWTONSMETHODACCURACY))
                {
                    return result;
                }
                else
                {
                    x = result;
                }
            }
            return new Complex(Complex.OtherValues.UNDEFINED); //No valid result the function diverges... probably
        }
        /**
         * This function is used to calculate the algebraic Taylor expansion
         * @param a The starting position of the Taylor expansion (For Maclurin Series use a=0)
         * @param variableExpanding The exact name of the variable expanding (NOTE: trying to expand an expression with more than one variable will throw an error)
         * @param maxPower The highest power value of the Taylor expansion
         * @return 
         */
        @Override
        public Term taylorSeries(Complex a, String variableExpanding, int maxPower)
        {
            int n; //Declare int 'i' for looping through the different powers
            Term F = this;
            ArrayList<Substitution> sub = new ArrayList<>();
            sub.add(new Substitution(variableExpanding, a));
            Term result = null;
            Term currentTerm;
            for (n = 0; n <= maxPower; n++)
            {
                currentTerm = new Operation (new Value(F.valueOf(sub).div(new Complex(n).factorial())), Operator.MULT, new Operation(new Operation(new Variable(variableExpanding), Operator.SUB, new Value(a)), Operator.POW, new Value(n)));
                if (result == null)
                {
                    result = currentTerm;
                }
                else
                {
                    result = new Operation(result, Operator.ADD, currentTerm);
                }
                F = F.differentiate(variableExpanding, "f("+variableExpanding+")", new ArrayList<>());
            }
            if (result != null)
            {
                return result.Simplify();
            }
            else
            {
                return result;
            }
        }
    }
    /**
     * This class will be used to store and process matricies
     */
    public static class Matrix extends Term
    {
        public final Term[][] MATRIX;
        
        public static Matrix identity(int size)
        {
            Matrix result = new Matrix(size);
            int ix;
            int iy;
            for (ix = 0; ix < result.getWidth(); ix++)
            {
                for (iy = 0; iy < result.getHeight(); iy++)
                {
                    Value value;
                    if (ix == result.getHeight()-iy-1)
                    {
                        value = new Value(1);
                    }
                    else
                    {
                        value = new Value(0);
                    }
                    result.MATRIX[ix][iy] = value;
                }
            }
            return result;
        }
        
        public Matrix (int width, int height)
        {
            this.MATRIX = new Term[width][height];
        }
        
        public Matrix (int size)
        {
            this.MATRIX = new Term[size][size];
        }
        
        public Matrix (Term[][] matrix)
        {
            this.MATRIX = matrix;
        }
        /**
         * The height of the matrix
         * @return Returns the height of the matrix
         */
        public int getWidth()
        {
            return MATRIX.length;
        }
        /**
         * The width of the matrix
         * @return Returns the width of the matrix
         */
        public int getHeight()
        {
            return MATRIX[0].length;
        }
        /**
         * Used to test if two matricies have the same dimensions
         * @param x The matrix to test against
         * @return Returns true if both matricies are identical in width and height
         */
        public boolean sameDimensions(Matrix x)
        {
            return this.getWidth() == x.getWidth() && this.getHeight() == x.getHeight();
        }
        /**
         * Used to decide if two matricies can be multiplied together
         * @param x
         * @return 
         */
        public boolean canMult(Matrix x)
        {
            return this.getWidth() == x.getHeight();
        }
        /**
         * 
         * @return Returns true if the matrix is square
         */
        public boolean isSquare()
        {
            return this.getWidth() == this.getHeight();
        }
        /**
         * Used for calculating the inverse and determinant
         * @param x
         * @param y
         * @return 
         */
        private double getPlusMinus(int x, int y)
        {
            if ((x + this.getHeight() - 1 - y) % 2 == 0) //The TOP LEFT corner must be positive
            {
                return 1.0;
            }
            else
            {
                return -1.0;
            }
        }
        /**
         * 
         * @param x
         * @return Returns a 2x1 matrix containing the dimensions (Width, Height) of a resulting matrix from multiplying these two matricies together
         */
        public Matrix resultMultDimensions(Matrix x)
        {
            if (!this.canMult(x)) 
            {
                throw new UnsupportedOperationException("Cannot multiply a " + this.getWidth() + "x" + this.getHeight() + " matrix to a " + x.getWidth() + "x" + x.getHeight() + " matrix");
            }
            else
            {
                Matrix dimensions = new Matrix(1, 2);
                dimensions.MATRIX[0][0] = new Value(x.getWidth());
                dimensions.MATRIX[0][1] = new Value(this.getHeight());

                return dimensions;
            }
        }
        /**
         * Adds two matricies together
         * @param x The second matrix
         * @return Returns the sum of the two matricies
         */
        public Matrix add(Matrix x)
        {
            if (this.sameDimensions(x))
            {
                int ix;
                int iy;
                
                Matrix result = new Matrix(this.getWidth(), this.getHeight());
                
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    for (iy = 0; iy < this.getHeight(); iy++)
                    {
                        result.MATRIX[ix][iy] = new Operation(this.MATRIX[ix][iy], Operation.Operator.ADD, x.MATRIX[ix][iy]).Simplify();
                    }
                }
                return result;
            }
            else
            {
                throw new UnsupportedOperationException("Cannot add a " + this.getWidth() + "x" + this.getHeight() + " matrix to a " + x.getWidth() + "x" + x.getHeight() + " matrix");
            }
        }
        
        /**
         * Adds two matricies together
         * @param x The second matrix
         * @return Returns the sum of the two matricies
         */
        public Matrix sub(Matrix x)
        {
            if (this.sameDimensions(x))
            {
                int ix;
                int iy;
                
                Matrix result = new Matrix(this.getWidth(), this.getHeight());
                
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    for (iy = 0; iy < this.getHeight(); iy++)
                    {
                        result.MATRIX[ix][iy] = new Operation(this.MATRIX[ix][iy], Operation.Operator.SUB, x.MATRIX[ix][iy]).Simplify();
                    }
                }
                return result;
            }
            else
            {
                throw new UnsupportedOperationException("Cannot subtract a " + this.getWidth() + "x" + this.getHeight() + " matrix by a " + x.getWidth() + "x" + x.getHeight() + " matrix");
            }
        }
        
        public Matrix mult(Matrix x)
        {
            
            if (this.canMult(x))
            {
                Matrix dimensions = this.resultMultDimensions(x);
                
                Matrix result = new Matrix((int) dimensions.MATRIX[0][0].valueOf().REAL, (int) dimensions.MATRIX[0][1].valueOf().REAL);
                
                int ix;
                int iy;
                int i;
                
                for (ix = 0; ix < result.getWidth(); ix++)
                {
                    for (iy = 0; iy < result.getHeight(); iy++)
                    {
                        Term currentTerm = null;
                        for (i = 0; i < this.getWidth(); i++)
                        {
                            Term currentMultiply = new Operation(this.MATRIX[i][iy], Operation.Operator.MULT, x.MATRIX[ix][x.getHeight()-i-1]);
                            if (currentTerm == null)
                            {
                                currentTerm = currentMultiply;
                            }
                            else
                            {
                                currentTerm = new Operation(currentTerm, Operation.Operator.ADD, currentMultiply);
                            }
                        }
                        if (currentTerm != null)
                        {
                            result.MATRIX[ix][iy] = currentTerm.Simplify();
                        }
                    }
                }
                
                return result;
            }
            else
            {
                throw new UnsupportedOperationException("Cannot multiply a " + this.getWidth() + "x" + this.getHeight() + " matrix by a " + x.getWidth() + "x" + x.getHeight() + " matrix");
            }
        }
        
        public Matrix pow(int power)
        {        
            if (power >= 1)
            {    
                Matrix result = this;
                //Raising something to the power the traditional way
                int i;
                for (i = 1; i < power; i++)
                {
                    result = result.mult(this);
                }
                return result;
            }
            else
            {
                throw new UnsupportedOperationException("Cannot raise a matrix to the power of a value that is not >= 1");
            }
        }
        /**
         * Calculates the determinate of a matrix
         * @return Returns an algebraic expression for this specific matrix
         */
        public Term det()
        {
            if (this.isSquare())
            { //If the matrix is square
                if (this.getWidth() == 1 && this.getHeight()== 1) //Base case
                {
                    return this.MATRIX[0][0];
                }
                else
                {
                    int ix; //declare int 'ix' for looping

                    Term result = null;

                    for (ix = 0; ix < this.getWidth(); ix++)
                    {
                        Term currentTerm = new Operation(new Value(getPlusMinus(ix, 0)), Operation.Operator.MULT, new Operation(this.MATRIX[ix][0], Operation.Operator.MULT, this.removeColumnRow(ix, 0).det()));
                        if (result == null) 
                        {
                            result = currentTerm;
                        }
                        else
                        {
                            result = new Operation(result, Operation.Operator.ADD, currentTerm);
                        }
                    }
                    if (result != null)
                    {
                        return result.Simplify(); //Simplify the result
                    }
                    else
                    {
                        return null;
                    }
                }
            }
            else
            {
                throw new UnsupportedOperationException("Cannot find the determinant of a " + this.getWidth() + "x" + this.getHeight() + " matrix (it MUST be SQUARE [rows=columns])");
            }
        }
        /**
         * Removes one specified column and row from the matrix
         * Useful for calculating the determinant
         * @param column The x position of the column to remove 
         * @param row The y position of the row to remove
         * @return Returns a matrix with this row and column missing
         */
        public Matrix removeColumnRow(int column, int row)
        {
            Matrix result = new Matrix(this.getWidth()- 1, this.getHeight() - 1);


            for (int newx = 0, oldx = 0; newx < result.getWidth(); newx++, oldx++)
            {
                for (int newy = 0, oldy = 0; newy < result.getHeight(); newy++, oldy++)
                {
                    if (oldx == column)
                    { //If I'm on the column that is not allowed
                        oldx++; //Skip this column
                    }
                    if (oldy == row)
                    { //If I'm on the row that is not allowed
                        oldy++; //Skip this row
                    }
                    if (oldx < this.getWidth() && oldy < this.getHeight())
                    {
                        result.MATRIX[newx] [newy] = this.MATRIX[oldx][oldy];
                    }
                }
            }
            return result;
        }
        /**
         * Transposes the matrix
         * @return returns a transposed/flipped matrix along the leading diagonal
         */
        public Matrix transpose()
        {
            int ix;
            int iy;
            Matrix result = new Matrix(this.getHeight(), this.getWidth());

            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    /*
                    This needs all this maths becuase the leading diagonal is drawn from the top left corner
                    */
                    result.MATRIX[result.getWidth()-1-iy][result.getHeight()-1-ix] = this.MATRIX[ix][iy]; //Flips the matrix along the leading diagonal
                }
            }
            return result;
        }
        /**
         * Scales the matrix (multiply every item by some scale factor)
         * @param scale The scale factor
         * @return Returns a scaled matrix
         */
        public Matrix scale(Term scale)
        {
            int ix;
            int iy;
            
            Matrix result = new Matrix(this.getWidth(), this.getHeight());
            
            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    result.MATRIX[ix][iy] = new Operation(this.MATRIX[ix][iy], Operation.Operator.MULT, scale).Simplify();
                }
            }
            return result;
        }
        /**
         * Returns true if two matricies are numerically the same given certain substitutions
         * @param x A matrix to test against this
         * @param sub any variable substitutions to make
         * @return Returns true if this == x
         */
        public boolean equals(Matrix x, ArrayList<Substitution>sub)
        {
            if (this.sameDimensions(x))
            { //If the matricies are the same dimensions
                int ix;
                int iy;
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    for (iy = 0; iy < this.getHeight(); iy++)
                    {
                        if (!this.MATRIX[ix][iy].valueOf(sub).equals(x.MATRIX[ix][iy].valueOf(sub)))
                        { //If two values are not the same
                            return false;
                        }
                    }
                }
                //If all values have been checked and are equal 
                return true;
            }
            else
            { //If the matricies aren't even the same dimensions
                return false;
            }
        }
        /**
         * Returns true if two matricies are numerically the same given certain substitutions
         * @param x A matrix to test against this
         * @param sub any variable substitutions to make
         * @param accuracy
         * @return Returns true if this == x
         */
        public boolean equals(Matrix x, ArrayList<Substitution>sub, double accuracy)
        {
            if (this.sameDimensions(x))
            { //If the matricies are the same dimensions
                int ix;
                int iy;
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    for (iy = 0; iy < this.getHeight(); iy++)
                    {
                        if (!this.MATRIX[ix][iy].valueOf(sub).equals(x.MATRIX[ix][iy].valueOf(sub), accuracy))
                        { //If two values are not the same
                            return false;
                        }
                    }
                }
                //If all values have been checked and are equal 
                return true;
            }
            else
            { //If the matricies aren't even the same dimensions
                return false;
            }
        }
        /**
         * Calculates the inverse of a matrix
         * @return Returns the inverse of the matrix such that M M-1 = I where I is the corresponding identity matrix 
         */    
        public Matrix inverse()
        {
            if (this.isSquare())
            {
                Matrix result = new Matrix(this.getWidth(), this.getHeight());

                int ix;
                int iy;
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    for (iy = 0; iy < this.getHeight(); iy++)
                    {

                        result.MATRIX[ix][iy] = new Operation(new Value(this.getPlusMinus(ix, iy)), Operation.Operator.MULT, this.removeColumnRow(ix, iy).det());
                    }
                }
                return (Matrix) result.transpose().scale(new Operation(new Value(1), Operation.Operator.DIV, this.det())).Simplify();

            }
            else
            {
                return null;
            }
        }
        /**
         * Used to determine if this matrix numerically is singular (Has no inverse)
         * @param sub Any values required to substitute in
         * @return Returns true if the matrix is singular
         */
        public boolean isSingular(ArrayList<Substitution> sub)
        {
            return this.det().valueOf(sub).isZero(); //The matrix is singular if det(M) == 0 as 1/det => 1/0 is undefined
        }
        /**
         * Converts the matrix to a string
         * @param notation The notation to use when writing the matrix
         * @return Returns a string representation of the matrix
         */
        @Override
        public String write(NotationType notation)
        {
            String result = "";
            int ix;
            int iy;
            for (iy = this.getHeight()-1; iy >= 0; iy--)
            {
                result += "| ";
                for (ix = 0; ix < this.getWidth(); ix++)
                {
                    result += this.MATRIX[ix][iy].write(notation) + " ";
                }
                result += "|\n";
            }
            return result;
        }
        /**
         * 
         * @param variable
         * @param resultingVariable
         * @return 
         */
        @Override
        public Term differentiate(String variable, String resultingVariable, ArrayList<Substitution> variablesIgnoring)
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        /**
         * 
         * @return 
         */
        @Override
        public boolean explicitlyContainsNoVariables()
        {
            int ix;
            int iy;
            //Iterate through all terms in the matrix
            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    if (!this.MATRIX[ix][iy].explicitlyContainsNoVariables())
                    { //If this term contains a variable
                        return false; //The matix does contain variable so return false
                    }
                }
            }
            //Hasn't returned false so the matrix must not contain any variables
            return true; //Return the matrix does not contain any variables
        }
        /**
         * Simplifies each term object in the matrix
         * @return Returns a simplified but equivalent matrix
         */
        @Override
        public Term Simplify()
        {
            int ix;
            int iy;
            
            Matrix result = new Matrix(this.getWidth(), this.getHeight());
            
            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    result.MATRIX[ix][iy] = this.MATRIX[ix][iy].Simplify();
                }
            }
            return result;
        }
        /**
         * This function is not used for matricies as it doesn't really make any sense for a grid of numbers
         * @param sub The values to sub into
         * @return Always throws an error
         */
        @Override
        public Complex valueOf(ArrayList<Substitution> sub)
        { //Ambigous Statement cannot return a single complex value for a matrix, Yes I am awear the determinate exists but that would require the det operator to be present
            throw new UnsupportedOperationException("There is not single value for a matrix, try using DET");
        }
        /**
         * This function is not used for matricies as it doesn't really make any sense for a grid of numbers
         * @return Always throws an error
         */
        @Override
        public Complex valueOf()
        { //Ambigous Statement cannot return a single complex value for a matrix, Yes I am awear the determinate exists but that would require the det operator to be present
            throw new UnsupportedOperationException("There is not single value for a matrix, try using DET");
        }
        /**
         * Makes a deep copy
         * @return 
         */
        @Override
        public Term copy()
        {
            Matrix result = new Matrix(this.getWidth(), this.getHeight());
            int ix;
            int iy;
            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    result.MATRIX[ix][iy] = this.MATRIX[ix][iy];
                }
            }
            
            return result;
        }
        /**
         * 
         * @param variable
         * @return 
         */
        @Override
        public Complex[] findMyRoots(String variable)
        {
            throw new UnsupportedOperationException("Root finding is not supported for matricies");
        }
        /**
         * 
         * @return 
         */
        @Override
        public String type()
        {
            return "Matrix";
        }
        /**
         * 
         * @param a
         * @param variableExpanding
         * @param maxPower
         * @return 
         */
        @Override
        public Term taylorSeries(Complex a, String variableExpanding, int maxPower)
        {
            throw new UnsupportedOperationException("The taylor series is not supported for matrices");
        }
        /**
         * Draws straight to the graph object
         * Graphs the current matrix description of points to
         * @param graph The graph to draw to
         * @param color The colour of the graph
         */
        public void graphTo(Graph graph, Color color)
        {
            if (this.getHeight() == 2)
            { //If a 2 high matrix for drawing 2d shapes
                if (this.getWidth() >= 2)
                {
                    Graphics2D g = graph.GRAPH.createGraphics();
                    g.setColor(color);      
                    g.setStroke(new BasicStroke(1 + (2 * BRUSHWIDTH)));
                    int i;
                    for (i = 0; i < this.getWidth(); i++)
                    {
                        int nextPoint = i+1;
                        if (nextPoint >= this.getWidth())
                        {
                            nextPoint -= this.getWidth();
                        }
                        int x1 = (int) ((this.MATRIX[i][0].valueOf().REAL - graph.XMIN) / graph.getXStep());
                        int y1 = graph.getHeightInPixels() - (int) ((this.MATRIX[i][1].valueOf().REAL - graph.YMIN) / graph.getYStep());
                        int x2 = (int) ((this.MATRIX[nextPoint][0].valueOf().REAL - graph.XMIN) / graph.getXStep());
                        int y2 = graph.getHeightInPixels() - (int) ((this.MATRIX[nextPoint][1].valueOf().REAL - graph.YMIN) / graph.getYStep());;
                        g.drawLine(x1, y1, x2, y2);
                    }
                    
                    g.dispose(); //No longer need to do any drawing
                }
            }
            else
            { //If not 2d co-ordiantes
                throw new UnsupportedOperationException("Cannot draw " + this.getHeight() + "d shapes"); 
            }
        }
        /**
         * Alows you to substitue values into a matrix containing algebra
         * @param sub
         * @return 
         */
        @Override
        public Matrix matrixValueOf(ArrayList<Substitution> sub)
        {
            Matrix result = new Matrix(this.getWidth(), this.getHeight());
            int ix;
            int iy;
            for (ix = 0; ix < this.getWidth(); ix++)
            {
                for (iy = 0; iy < this.getHeight(); iy++)
                {
                    result.MATRIX[ix][iy] = new Value(this.MATRIX[ix][iy].valueOf(sub)).Simplify();
                }
            }
            return result;
        }
        /**
         * 
         * @return 
         */
        @Override
        public Matrix matrixValueOf()
        {
            ArrayList<Substitution> sub = new ArrayList<>();
            return this.matrixValueOf(sub); //Return myself
        }
    
    }
    /**
     * Stores equations of all types using two expression
     */
    public static class Equation
    {
        /**
         * Stores the different types of equation
         * 
         * KEY
         * 
         * EQ - Equal (=)
         * GT - Greater Than (>)
         * GTEQ - Greater Than or Equal To (>=)
         * LT - Less Than (<)
         * LTEQ - Less Than Or Equal To (<=)
         */
        public static enum EquationType
        {
            EQ,
            GT,
            GTEQ,
            LT,
            LTEQ,
        }
        
        public final EquationType EQUATOR; //Stores the main equation symbol
        
        public final Term EXPRESSION1; //Operation 1
        public final Term EXPRESSION2; //Operation 2
        
        /**
         * Makes an equation
         * @param expression1
         * @param equator
         * @param expression2 
         */
        public Equation(Term expression1, EquationType equator, Term expression2)
        {
            this.EXPRESSION1 = expression1;
            this.EQUATOR = equator;
            this.EXPRESSION2 = expression2;
        }
        /**
         * Writes the equation to a string
         * @param notation
         * @return 
         */
        public String write(NotationType notation)
        {
            String equator = "";
            if (null != EQUATOR)
            switch (EQUATOR)
            {
                case EQ:
                    equator = " = ";
                    break;
                case GT:
                    equator = " > ";
                    break;
                case GTEQ:
                    equator = " >= ";
                    break;
                case LT:
                    equator = " < ";
                    break;
                case LTEQ:
                    equator = " <= ";
                    break;
                default:
                    break;
            }
            else
            {
                
            }
            
            return EXPRESSION1.write(notation) + equator + EXPRESSION2.write(notation);
        }
        /**
         * Simplifies the equation
         * @return 
         */
        public Equation simplify()
        {
            return new Equation(EXPRESSION1.Simplify(), EQUATOR, EXPRESSION2.Simplify());
        }
        /**
         * Is the equation currently equal
         * @param sub
         * @param accuracy
         * @return 
         */
        public boolean isEqual(ArrayList<Substitution> sub, double accuracy)
        {
            return EXPRESSION1.valueOf(sub).equals(EXPRESSION2.valueOf(sub), accuracy);
        }
        /**
         * Is the equation currently greater than
         * @param sub
         * @return 
         */
        public boolean isGreaterThan(ArrayList<Substitution> sub)
        {
            return EXPRESSION1.valueOf(sub).isGreaterThan(EXPRESSION2.valueOf(sub));
        }
        /**
         * is the equation currently less than
         * @param sub
         * @return 
         */
        public boolean isLessThan(ArrayList<Substitution> sub)
        {
            return EXPRESSION1.valueOf(sub).isLessThan(EXPRESSION2.valueOf(sub));
        }
        /**
         * Draws straight to the graph object
         * Graphs the current equation to
         * @param graph The graph to draw to
         * @param color The colour of the graph
         * @param xAxis The variable plotted along the x axis
         * @param isImaginaryX Is this axis imaginary
         * @param yAxis The variable plotted along the y axis
         * @param isImaginaryY Is this axis imaginary
         */
        public synchronized void graphTo(Graph graph, Color color, String xAxis, boolean isImaginaryX, String yAxis, boolean isImaginaryY)
        {
            if (this.EXPRESSION1 == null || this.EXPRESSION2 == null)
            {
                throw new UnsupportedOperationException("Cannot graph an enquation with null values"); 
            }
            if (graph == null)
            {
                throw new UnsupportedOperationException("Cannot graph with null graph"); 
            }
            int ix; //Used for looping on the x axis
            int iy; //Used for looping on the y axis
            
            double AAX; //Used for anti alising along the x axis
            double AAY; //Used for anti alising along the y axis

            // Create a graphics which can be used to draw into the buffered image
            Graphics2D g = graph.GRAPH.createGraphics();
            g.setColor(color);      
            /*
            Iterate through all the pixel points
            */
            for (ix = 0; ix < graph.getWidthInPixels(); ix++)
            {
                for (iy = 0; iy < graph.getHeightInPixels(); iy++)
                {
                    boolean shouldDraw = false;
                    /*
                    Find multiple points in the same pixel and if any one of them would be equal paint the pixel the line colour
                    
                    I've called this anti aliasing but that involves some avergae of all the points, this doesn't
                    
                    I used this method cause steep lines could become splotchy at higher accuracy values
                    */
                    //System.out.println("TESTPOINT X: " + ix + " Y: " + iy);
                    Draw:
                    for (AAX = 0; AAX < graph.getXStep(); AAX += graph.getXStep() / ((double) AAAMOUNT))
                    {
                        for (AAY = 0; AAY < graph.getYStep(); AAY += graph.getYStep() / ((double) AAAMOUNT))
                        {
                            double xPos = graph.XMIN + (graph.getXStep() * ((double) ix)) + AAX;
                            double yPos = graph.YMAX - (graph.getYStep() * ((double) iy)) + AAY; //Y inverted in images henc starting from the top and working down

                            //Create the substitution
                            ArrayList<Substitution>sub = new ArrayList<>();
                            if (isImaginaryX)
                            {
                                sub.add(new Substitution(xAxis, new Complex(0, xPos)));
                            }
                            else
                            {
                                sub.add(new Substitution(xAxis, xPos));
                            }
                            if (isImaginaryY)
                            { //Complex
                                if (sub.get(0).NAME.equals(yAxis))
                                {
                                    sub.set(0, new Substitution(yAxis, new Complex(sub.get(0).VALUE.REAL, yPos)));
                                }
                                else
                                {
                                    sub.add(new Substitution(yAxis, new Complex(0, yPos)));
                                }
                            }
                            else
                            { //real
                                if (sub.get(0).NAME.equals(yAxis))
                                {
                                    sub.set(0, new Substitution(yAxis, new Complex(yPos, sub.get(0).VALUE.IMAGINARY)));
                                }
                                else
                                {
                                    sub.add(new Substitution(yAxis, yPos));
                                }
                            }

                            try
                            {
                                if (null != EQUATOR)
                                switch (EQUATOR)
                                {
                                    case EQ:
                                        //Equal to
                                        if (this.isEqual(sub, GRAPHINGACCURACY))
                                        { //If "equal"
                                            shouldDraw = true; //We should draw this pixel
                                            break Draw;
                                        }   break;
                                    case GT:
                                    case GTEQ:
                                        //Greater than
                                        if (isGreaterThan(sub))
                                        { //If "Greater than"
                                            shouldDraw = true; //We should draw this pixel
                                            break Draw;
                                        }   break;
                                    case LT:
                                    case LTEQ:
                                        //Less than
                                        if (isLessThan(sub))
                                        { //If "Less than"
                                            shouldDraw = true; //We should draw this pixel
                                            break Draw;
                                        }   break;
                                    default:
                                        break;
                                }
                                else
                                {

                                }
                            }
                            catch (UnsupportedOperationException e)
                            {

                            }
                        }
                    }
                    if (shouldDraw)
                    {
                        g.fillOval(ix - BRUSHWIDTH, iy - BRUSHWIDTH, 1 + (2 * BRUSHWIDTH), 1 + (2 * BRUSHWIDTH));
                    }
                }
            }
            // Disposes of this graphics context and releases any system resources that it is using.
            g.dispose();
        }
        /**
         * Draws straight to the graph object
         * Graphs the current equation to
         * @param graph The graph to draw to
         * @param color The colour of the graph
         * @param theta
         * @param r
         */
        public synchronized void graphPolarTo(Graph graph, Color color, String theta, String r)
        {
            if (this.EXPRESSION1 == null || this.EXPRESSION2 == null)
            {
                throw new UnsupportedOperationException("Cannot graph an enquation with null values"); 
            }
            if (graph == null)
            {
                throw new UnsupportedOperationException("Cannot graph with null graph"); 
            }
            int ix; //Used for looping on the x axis
            int iy; //Used for looping on the y axis
            
            double AAX; //Used for anti alising along the x axis
            double AAY; //Used for anti alising along the y axis

            // Create a graphics which can be used to draw into the buffered image
            Graphics2D g = graph.GRAPH.createGraphics();
            g.setColor(color);      
            /*
            Iterate through all the pixel points
            */
            for (ix = 0; ix < graph.getWidthInPixels(); ix++)
            {
                for (iy = 0; iy < graph.getHeightInPixels(); iy++)
                {
                    boolean shouldDraw = false;
                    /*
                    Find multiple points in the same pixel and if any one of them would be equal paint the pixel the line colour
                    
                    I've called this anti aliasing but that involves some avergae of all the points, this doesn't
                    
                    I used this method cause steep lines could become splotchy at higher accuracy values
                    */
                    //System.out.println("TESTPOINT X: " + ix + " Y: " + iy);
                    Draw:
                    for (AAX = 0; AAX < graph.getXStep(); AAX += graph.getXStep() / ((double) AAAMOUNT))
                    {
                        for (AAY = 0; AAY < graph.getYStep(); AAY += graph.getYStep() / ((double) AAAMOUNT))
                        {
                            for (int nPi = -5; nPi <= 5; nPi++)
                            {
                                double xPos = graph.XMIN + (graph.getXStep() * ((double) ix)) + AAX;
                                double yPos = graph.YMAX - (graph.getYStep() * ((double) iy)) + AAY; //Y inverted in images henc starting from the top and working down

                                //This is polar co-ordinates so convert to polar (angle and magnitude)
                                double angle = Math.atan2(yPos, xPos) + (nPi * Math.PI);
                                double magnitude = Math.sqrt((xPos*xPos)+(yPos*yPos));

                                //Create the substitution
                                ArrayList<Substitution>sub = new ArrayList<>();
                                sub.add(new Substitution(r, magnitude));
                                sub.add(new Substitution(theta, angle));
                                try
                                {
                                    if (null != EQUATOR)
                                    switch (EQUATOR)
                                    {
                                        case EQ:
                                            //Equal to
                                            if (this.isEqual(sub, GRAPHINGACCURACY))
                                            { //If "equal"
                                                shouldDraw = true; //We should draw this pixel
                                                break Draw;
                                            }   break;
                                        case GT:
                                        case GTEQ:
                                            //Greater than
                                            if (isGreaterThan(sub))
                                            { //If "Greater than"
                                                shouldDraw = true; //We should draw this pixel
                                                break Draw;
                                            }   break;
                                        case LT:
                                        case LTEQ:
                                            //Less than
                                            if (isLessThan(sub))
                                            { //If "Less than"
                                                shouldDraw = true; //We should draw this pixel
                                                break Draw;
                                            }   break;
                                        default:
                                            break;
                                    }
                                    else
                                    {

                                    }
                                }
                                catch (UnsupportedOperationException e)
                                {

                                }
                            }
                        }
                    }
                    if (shouldDraw)
                    {
                        g.fillOval(ix - BRUSHWIDTH, iy - BRUSHWIDTH, 1 + (2 * BRUSHWIDTH), 1 + (2 * BRUSHWIDTH));
                    }
                }
            }
            // Disposes of this graphics context and releases any system resources that it is using.
            g.dispose();
        }
        public void plotXIntercepts(Graph graph, Color color, String xAxis, boolean isImaginaryX, String yAxis, boolean isImaginaryY)
        {
            Equation xAxisEQ = new Equation(new Variable(yAxis), Equation.EquationType.EQ, new Value(0));
            plotIntercepts(graph, color, xAxis, isImaginaryX, yAxis, isImaginaryY, xAxisEQ);
        }
        public void plotYIntercepts(Graph graph, Color color, String xAxis, boolean isImaginaryX, String yAxis, boolean isImaginaryY)
        {
            Equation yAxisEQ = new Equation(new Variable(xAxis), Equation.EquationType.EQ, new Value(0));
            plotIntercepts(graph, color, xAxis, isImaginaryX, yAxis, isImaginaryY, yAxisEQ);
        }
        public void plotIntercepts(Graph graph, Color color, String xAxis, boolean isImaginaryX, String yAxis, boolean isImaginaryY, Equation equation2)
        {
            int ix; //Used for looping on the x axis
            int iy; //Used for looping on the y axis
            
            double AAX; //Used for anti alising along the x axis
            double AAY; //Used for anti alising along the y axis
            
            ArrayList<Complex>xPoints = new ArrayList<>();
            ArrayList<Complex>yPoints = new ArrayList<>();
            Complex x = new Complex();
            Complex y = new Complex();
            
            
            
            // Create a graphics which can be used to draw into the buffered image
            Graphics2D g = graph.GRAPH.createGraphics();
            g.setColor(color);   
            g.setFont(new Font("Arial Black", Font.TRUETYPE_FONT, 15));
            /*
            Iterate through all the pixel points
            */
            for (ix = 0; ix < graph.getWidthInPixels(); ix++)
            {
                for (iy = 0; iy < graph.getHeightInPixels(); iy++)
                {
                    boolean shouldDraw = false;
                    /*
                    Find multiple points in the same pixel and if any one of them would be equal paint the pixel the line colour
                    
                    I've called this anti aliasing but that involves some avergae of all the points, this doesn't
                    
                    I used this method cause steep lines could become splotchy at higher accuracy values
                    */
                    for (AAX = 0; AAX < graph.getXStep(); AAX += graph.getXStep() / ((double) AAAMOUNT))
                    {
                        for (AAY = 0; AAY < graph.getYStep(); AAY += graph.getYStep() / ((double) AAAMOUNT))
                        {
                            double xPos = graph.XMIN + (graph.getXStep() * ((double) ix)) + AAX;
                            double yPos = graph.YMAX - (graph.getYStep() * ((double) iy)) + AAY; //Y inverted in images henc starting from the top and working down

                            //Create the substitution
                            ArrayList<Substitution>sub = new ArrayList<>();
                            if (isImaginaryX)
                            {
                                sub.add(new Substitution(xAxis, new Complex(0, xPos)));
                            }
                            else
                            {
                                sub.add(new Substitution(xAxis, xPos));
                            }
                            if (isImaginaryY)
                            { //Complex
                                if (sub.get(0).NAME.equals(yAxis))
                                {
                                    sub.set(0, new Substitution(yAxis, new Complex(sub.get(0).VALUE.REAL, yPos)));
                                }
                                else
                                {
                                    sub.add(new Substitution(yAxis, new Complex(0, yPos)));
                                }
                            }
                            else
                            { //real
                                if (sub.get(0).NAME.equals(yAxis))
                                {
                                    sub.set(0, new Substitution(yAxis, new Complex(yPos, sub.get(0).VALUE.IMAGINARY)));
                                }
                                else
                                {
                                    sub.add(new Substitution(yAxis, yPos));
                                }
                            }

                            try
                            {
                                if (this.isEqual(sub, INTERCEPTSACCURACY) && equation2.isEqual(sub, INTERCEPTSACCURACY))
                                { //If "equal"
                                    
                                    if (isImaginaryX)
                                    {
                                        x = new Complex(0, xPos);
                                    }
                                    else
                                    {
                                        x = new Complex(xPos);
                                    }
                                    if (isImaginaryY)
                                    {
                                        y = new Complex(0, yPos);
                                    }
                                    else
                                    {
                                        y = new Complex(yPos);
                                    }
                                    boolean alreadyContains = false;
                                    int iList;
                                    
                                    for (iList = 0; iList < xPoints.size(); iList++)
                                    {
                                        if (xPoints.get(iList).equals(x, INTERCEPTSEQUALACCURACY) && yPoints.get(iList).equals(y, INTERCEPTSEQUALACCURACY))
                                        {
                                            alreadyContains = true;
                                        }
                                    }
                                    if (!alreadyContains)
                                    {
                                        shouldDraw = true; //We should draw this pixel
                                        xPoints.add(x);
                                        yPoints.add(y);
                                    }
                                }  
                            }
                            catch (UnsupportedOperationException e)
                            {

                            }
                        }
                    }
                    if (shouldDraw)
                    {
                        
                        g.fillOval(ix - (2 * BRUSHWIDTH), iy - (2 * BRUSHWIDTH), 1 + (4 * BRUSHWIDTH), 1 + (4 * BRUSHWIDTH));
                        g.setColor(Color.BLACK);
                        g.drawString("(" + x.write(1) + ", " + y.write(1) + ")", ix, iy);
                        g.drawString("(" + x.write(1) + ", " + y.write(1) + ")", ix-1, iy-1);
                        g.setColor(color);
                        g.drawString("(" + x.write(1) + ", " + y.write(1) + ")", ix-2, iy-2);
                    }
                }
            }
            // Disposes of this graphics context and releases any system resources that it is using.
            g.dispose();
        }
        
        /**
         * this functions returns the specific differential e.g. d/dVARIABLE(EXPRESSION) where 
         * @param variable The variable to differentiate with respect to
         * @param resultingVariable The variable the function is equal to e.g y in example y = f(x) where y becomes dy/dx
         * @return Returns the differential of the expression
         */
        public Equation differentiate(String variable, String resultingVariable)
        { 
            if (EQUATOR == EquationType.EQ)
            {
                return new Equation(EXPRESSION1.differentiate(variable, resultingVariable, new ArrayList<>()), EQUATOR, EXPRESSION2.differentiate(variable, resultingVariable, new ArrayList<>()));
            }
            else
            {
                throw new UnsupportedOperationException("Cannot differentiate functions in the form: " + EQUATOR + " (can only differentitate functions with equals signs)");
            }
        }
        
        /**
         * Finds the roots of an expression which satisfy f(x)=0 where the expression if f(x)
         * @param variable The variable for x
         * @return Returns a list of approximate roots note this may not be every root
         */
        public Complex[] solve(String variable)
        {
            if (EQUATOR == EquationType.EQ)
            {
                Term Fx = expressAsFunctionEqualToZero(); //Rearange f(x) = g(x) to f(x) - g(x) = 0
                return Fx.findMyRoots(variable);
            }
            else
            {
                throw new UnsupportedOperationException("Cannot solve equations in the form: " + EQUATOR + " (can only solve equations with equals signs)");
            }
        }
        /**
         * Rearranges an equation in the form g(x,y,z...) =f(x,y,z...) to g(x,y,z...)-f(x,y,z...)=0
         * @return 
         */
        public Term expressAsFunctionEqualToZero()
        {
            return new Operation(EXPRESSION1, Operation.Operator.SUB, EXPRESSION2); //Rearange g(x,y,z...) =f(x,y,z...) to g(x,y,z...)-f(x,y,z...)=0
        }
    }
    /**
     * Used to store systems of equations
     */
    public static class EquationSystem
    {
        public final Equation[] EQUATIONS;
        
        public EquationSystem(Equation[] equations)
        {
            this.EQUATIONS = equations;
        }
        
        /**
         * Will solve all the equations using the newton rasphon method
         * @param variables
         * @return 
         */
        public ArrayList<Matrix> solve(ArrayList<String> variables)
        {
            Matrix functionVector = this.generateFunctionVector();
            Matrix jacobian = generateJacobianMatrix(functionVector, variables);

            ArrayList<Matrix>solutions = new ArrayList<>();
            
            generateIntialValues(new Matrix(1, variables.size()), 0, variables, functionVector, jacobian, solutions);
            
            return solutions;
        }
        
        private final static ArrayList<Substitution>EMPTYSUB = new ArrayList<>();
        /**
         * 
         * @param initial
         * @param level
         * @param VARIABLES
         * @param FUNCTIONVECTOR
         * @param JACOBIAN
         * @param solutions 
         */
        private void generateIntialValues(Matrix initial, int level, final ArrayList<String> VARIABLES, final Matrix FUNCTIONVECTOR, final Matrix JACOBIAN, ArrayList<Matrix>solutions)
        {
            double real;
            double img;
            if (level >= VARIABLES.size())
            {
                Matrix result = newtonRaphsonEquationSystemMethod(initial, VARIABLES, FUNCTIONVECTOR, JACOBIAN);
                
                if (result != null)
                {
                    boolean alreadyContainsSolution = false;
                    for (Matrix m : solutions)
                    {
                        if (m.equals(result, EMPTYSUB, ROOTSEQUALACCURACY*SIMULTEONUSACCURACYMODIFIER))
                        {
                            alreadyContainsSolution = true;
                        }
                    }
                    if (!alreadyContainsSolution)
                    {
                        solutions.add(result);
                    }
                }
                else
                {
                }
            }
            else
            {
                for (real = REALMIN/2.0; real <= REALMAX/2.0; real += REALSTEP*2.0)
                {
                    for (img = IMGMIN/2.0; img <= IMGMAX/2.0; img += IMGSTEP*2.0)
                    {
                        initial = (Matrix) initial.copy();
                        initial.MATRIX[0][level] = new Value(new Complex(real, img));
                        generateIntialValues(initial, level+1, VARIABLES, FUNCTIONVECTOR, JACOBIAN, solutions);
                    }
                }
            }
        }

        private final static double SIMULTEONUSACCURACYMODIFIER = 10000;
        /**
         * 
         * @param x
         * @param VARIABLES
         * @param FUNCTIONVECTOR
         * @param JACOBIAN
         * @return 
         */
        private Matrix newtonRaphsonEquationSystemMethod(Matrix x, final ArrayList<String> VARIABLES, final Matrix FUNCTIONVECTOR, final Matrix JACOBIAN)
        {
            int i;
            int i2;
            
            for (i = 0; i < ITERATIONCOUNT/20.0; i++)
            {
                ArrayList<Substitution> sub = new ArrayList<>();
                for (i2 = 0; i2 < VARIABLES.size(); i2++)
                {
                    sub.add(new Substitution(VARIABLES.get(i2), x.MATRIX[0][i2].valueOf()));
                }
                Matrix Jk = JACOBIAN.matrixValueOf(sub);
                Matrix Fk = (Matrix) FUNCTIONVECTOR.matrixValueOf(sub).scale(new Value(-1)).Simplify();

                Matrix dx = (Jk.inverse()).mult(Fk);

                Matrix newX = (Matrix) x.add(dx).Simplify();

                if (newX.equals(x, EMPTYSUB, NEWTONSMETHODACCURACY*SIMULTEONUSACCURACYMODIFIER))
                { //if the two vectors/matrices are equal
                    return newX;
                }
                x = newX;
            }
            return null; //No valid solution was found
        }
        /**
         * Useful for solving non linear simultaneous equations
         * @return Returns a vector containing algebraic expression for the different equations in the system expressed as functions equal to 0
         */
        public Matrix generateFunctionVector()
        {
            int i;
            Matrix function = new Matrix(1, this.EQUATIONS.length);
            //Iterate for all equations
            for (i = 0; i < this.EQUATIONS.length; i++)
            {
                function.MATRIX[0][i] = this.EQUATIONS[i].expressAsFunctionEqualToZero(); //Rearange g(x,y,z...) =f(x,y,z...) to g(x,y,z...)-f(x,y,z...)=0
            }
            return function;
        }
        /**
         * 
         * @param functionVector
         * @param variables
         * @return 
         */
        private static Matrix generateJacobianMatrix(Matrix functionVector, ArrayList<String> variables)
        {
            Matrix jacobian = new Matrix(variables.size(), functionVector.getHeight());
            int ix;
            int iy;
            int i;
            for (ix = 0; ix < jacobian.getWidth(); ix++)
            {
                for (iy = 0; iy < jacobian.getHeight(); iy++)
                {
                    String variable = "";
                    ArrayList<Substitution> variablesIgnoring = new ArrayList<>();
                    for (i = 0; i < variables.size(); i++)
                    {
                        if (ix != i)
                        {
                            variablesIgnoring.add(new Substitution(variables.get(i), 69)); //I chose 69 because it is a nice number
                        }
                        else
                        {
                            variable = variables.get(i);
                        }
                    }
                    jacobian.MATRIX[ix][iy] = functionVector.MATRIX[0][iy].differentiate(variable, "", variablesIgnoring);
                }
            }
            
            return (Matrix) jacobian.Simplify();
        }
    }
    /**
     * Used for drawing graphs
    */
    public static class Graph
    {
        public final BufferedImage GRAPH;
        public final double XMIN;
        public final double XMAX;
        public final double YMIN;
        public final double YMAX;

        /**
         * 
         * @return Returns the step up on the x axis as I go RIGHT ONE pixel
         */
        public final double getXStep()
        {
            return (XMAX - XMIN) / ((double) this.getWidthInPixels());
        }
        /**
         * 
         * @return Returns the step up on the y axis as I go UP ONE pixel
         */
        public final double getYStep()
        {
            return (YMAX - YMIN) / ((double) this.getHeightInPixels());
        }
        /**
         * 
         * @return Returns the width of the graph in pixels
         */
        public final int getWidthInPixels()
        {
            return GRAPH.getWidth();
        }
        /**
         * 
         * @return Returns the height of the graph in pixels
         */
        public final int getHeightInPixels()
        {
            return GRAPH.getHeight();
        }
        /**
         * Creates a graph
         * @param xMin Minimum (Left) X-Co-ordinate
         * @param xMax Maximum (Right) X-Co-ordinate
         * @param yMin Minimum (Left) Y-Co-ordinate
         * @param yMax Maximum (Right) Y-Co-ordinate
         * @param width Width in pixels
         * @param height height in pixels
         * @param bgColor
         */
        public Graph (double xMin, double xMax, double yMin, double yMax, int width, int height, Color bgColor)
        {
            GRAPH = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            this.XMIN = xMin;
            this.XMAX = xMax;
            this.YMIN = yMin;
            this.YMAX = yMax;

            // Create a graphics which can be used to draw into the buffered image
            Graphics2D g = this.GRAPH.createGraphics();
            g.setColor(bgColor);   
            g.fillRect(0, 0, this.getWidthInPixels(), this.getHeightInPixels());
            double step = 1;
            double i;
            g.setColor(new Color(255 - bgColor.getRed(), 255 - bgColor.getBlue(), 255 - bgColor.getGreen()));
            
            //System.out.println("XMIN: " + XMIN + "XMAX: " + XMAX + "YMIN: " + YMIN + "YMAX: " + YMAX);
            
            //If I can draw the Y AXIS
            if (XMIN < 0 && XMAX > 0)
            { //Draw the y Axis
                //System.out.println("CAN DRAW X");
                int xPos = (int) (Math.abs(XMIN) / this.getXStep()); //Find the x position of the y axis
                int yPos;
                g.fillRect(xPos - BRUSHWIDTH, 0, 1 + (2*BRUSHWIDTH), this.getHeightInPixels());
                for (i = YMIN; i < YMAX; i += step)
                {
                    yPos = (int) ((i-YMIN) / this.getYStep()); //Find the x position of the y axis
                    g.fillRect(xPos - BRUSHWIDTH, yPos-BRUSHWIDTH, 1 + (4*BRUSHWIDTH), 1 + (2*BRUSHWIDTH));
                }
                
            }

            //If I can draw the X AXIS
            if (YMIN < 0 && YMAX > 0)
            { //Draw the x Axis
                //System.out.println("CAN DRAW Y");
                int xPos;
                int yPos = (int) (Math.abs(YMIN) / this.getYStep()); //Find the y position of the y axis
                g.fillRect(0, yPos - BRUSHWIDTH, this.getWidthInPixels(), 1 + (2*BRUSHWIDTH));
                for (i = XMIN; i < XMAX; i += step)
                {
                    xPos = (int) ((i-XMIN) / this.getXStep()); //Find the x position of the y axis
                    g.fillRect(xPos - BRUSHWIDTH, yPos-BRUSHWIDTH, 1 + (2*BRUSHWIDTH), 1 + (4*BRUSHWIDTH));
                }
            }

            g.dispose();
            
            //saveGraph(this, "poo");
        }

        /**
        * Saves the graph
        * @param filename 
        */
       public void saveGraph(String filename)
       {
           try
           {     
               // Save as PNG
               File file = new File(filename+".png");
               ImageIO.write(this.GRAPH, "png", file);
           }
           catch (IOException ex)
           {
               //Gets here if failed to save graph
           }
       }
    }

    public static Equation stringToEquation(String expression, NotationType notationType)
    {
        int i;
        for (i = 0; i < expression.length()-1; i++)
        {
            if (expression.substring(i, i+1).equals("="))
            {
                return new Equation(stringToExpression(expression.substring(0, i), notationType), Equation.EquationType.EQ, stringToExpression(expression.substring(i+1, expression.length()), notationType));
            }
            else if (expression.substring(i, i+1).equals(">"))
            {
                return new Equation(stringToExpression(expression.substring(0, i), notationType), Equation.EquationType.GT, stringToExpression(expression.substring(i+1, expression.length()), notationType));
            }
            else if (expression.substring(i, i+1).equals("<"))
            {
                return new Equation(stringToExpression(expression.substring(0, i), notationType), Equation.EquationType.LT, stringToExpression(expression.substring(i+1, expression.length()), notationType));
            }
            else if (expression.substring(i, i+2).equals(">="))
            {
                return new Equation(stringToExpression(expression.substring(0, i), notationType), Equation.EquationType.GTEQ, stringToExpression(expression.substring(i+2, expression.length()), notationType));
            }
            else if (expression.substring(i, i+2).equals("<="))
            {
                return new Equation(stringToExpression(expression.substring(0, i), notationType), Equation.EquationType.LTEQ, stringToExpression(expression.substring(i+2, expression.length()), notationType));
            }
        }
        return null;
    }

    /**
     * Sets up a data structure from a string in some notation
     * 
     * KEYWORDS are specified INSIDE THE SWITCH STATEMENT
     * 
     * @param expression An expression written in some notation
     * @param notationType The type of notation to use
     * @return Returns the expression
     */
    public static Term stringToExpression(String expression, NotationType notationType)
    {
        String formattedExpression;
        String pre;
        String post;
        if (notationType == NotationType.INFIX)
        {
            /*
                I don't know why
                I don't flipping want to know why
                But regex escape characters in java need to escape twice because how strings are interpeted in java
                but that's stupid like why
                I thought java was about readablility or something
                This is sooooooo unreadable
                I don't care if it's nessicary I hate it
                like you need one slash to indicate the other one should be real to make the other one then dissapear when it's interpreted in java but idk
                like imagine if this wasn't a thing
                the universe would probably be a better place
                elon musk would've probably already colonised mars or something like that
            </rant>
            */
            expression = expression.replaceAll("\\) *\\(", ") * ("); //THIS IS MY PAIN

            
            formattedExpression = "";
            //Make sure there's spaces in between all of the lovely operators so it doesn't explode when trying to work out whats an opertaor, whats a number and whats a variable
            int i;
            for (i = 0; i < expression.length(); i++)
            {
                String currentText = expression.substring(i, i+1);
                switch (currentText)
                {
                    case "+":
                    case "*":
                    case "/":
                    case "!":
                    case "^":
                    case "(":
                    case ")":
                        pre = "";
                        post = "";
                        if (formattedExpression.length() > 0)
                        {
                            if (!formattedExpression.substring(formattedExpression.length()-1).equals(" "))
                            {
                                pre = " ";
                            }
                        }
                        if (expression.length() > i+1)
                        {
                            if (!expression.substring(i+1, i+2).equals(" "))
                            {
                                post = " ";
                            }
                        }
                        formattedExpression += pre + currentText + post;
                        break;

                    case "-":
                        boolean hasPlaced = false;
                        for (int i2 = i-1; i2 >= 0; i2--)
                        {
                            //Operator
                            if (Character.isDigit(expression.charAt(i2)) || Character.isAlphabetic(expression.charAt(i2)) || expression.charAt(i2) == ')' || expression.charAt(i2) == '!')
                            {
                                pre = "";
                                post = "";
                                if (expression.length() > i+1)
                                {
                                    if (expression.charAt(i+1) != ' ')
                                    {
                                        post = " ";
                                    }
                                }
                                if (formattedExpression.charAt(formattedExpression.length()-1) != ' ')
                                {
                                    pre = " ";
                                }
                                formattedExpression += pre + currentText + post;
                                hasPlaced = true;
                                break;
                            }
                            //Value
                            if (expression.charAt(i2) == '(' || expression.charAt(i2) == '+' || expression.charAt(i2) == '-' || expression.charAt(i2) == '*' || expression.charAt(i2) == '/' || expression.charAt(i2) == '^')
                            {
                                if (expression.length() > i+1)
                                {
                                    if (Character.isAlphabetic(expression.charAt(i+1)))
                                    {
                                        formattedExpression += "-1 * ";
                                        hasPlaced = true;
                                        break;
                                    }
                                }
                                if (hasPlaced == false)
                                {
                                    formattedExpression += currentText;
                                    hasPlaced = true;
                                }
                                break;
                            }
                        }
                        if (hasPlaced == false)
                        {
                            if (expression.length() > i+1)
                                {
                                    if (Character.isAlphabetic(expression.charAt(i+1)))
                                    {
                                        formattedExpression += "-1 * ";
                                        hasPlaced = true;
                                    }
                                }
                                if (hasPlaced == false)
                                {
                                    formattedExpression += currentText;
                                    hasPlaced = true;
                                }
                        }
                        break;

                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                        if (expression.length() > i+1)
                        {
                            if (expression.charAt(i+1) == '(' || Character.isAlphabetic(expression.charAt(i+1)))
                            {
                                formattedExpression += currentText + " * ";
                                break;
                            }
                        }
                    default:
                        formattedExpression += currentText;
                        break;
                }
            }
            //System.out.println("FORMAT: " + formattedExpression);
        }
        else
        {
            formattedExpression = expression;
        }
        String[] items = formattedExpression.split(" ");
        int startingPos = 0;
        if (notationType == NotationType.INFIX) 
        {
            return stringToExpression(infixToPostFix(items), NotationType.POSTFIX);
        }
        if (notationType == NotationType.POSTFIX)
        {
            startingPos = items.length - 1;
        }
        return generateTerm(items, startingPos, notationType).RESULTANTTERM;
    }
    /**
     * Used to return all the necessary information from building up an expression which it itself is used as an argument
     */
    public static class ArgumentObject
    {
        
        public final Term RESULTANTTERM;
        public final int ENDINGPOSITION;
        
        public ArgumentObject(Term resultantTerm, int endingPosition)
        {
            this.RESULTANTTERM = resultantTerm;
            this.ENDINGPOSITION = endingPosition;
        }
    }
    /**
     * Used to describe the 3 different types of maths notation
     * Prefix - Operator goes before arguments e.g. * 6 7 = 42
     * Infix - Standard way to write maths e.g. 6 * 7 = 42
     * Postfix - Operator comes after the arguments e.g. 6 7 * = 42
     */
    public enum NotationType
    {
        PREFIX,
        INFIX,
        POSTFIX
    }
    private static String infixToPostFix(String[] items)
    {
        ArrayList<String> stack = new ArrayList<>();
        ArrayList<Integer> stackOrder = new ArrayList<>();
        String result = "";
        int i;
        int relativePriority = 0;
        for (i = 0; i < items.length; i++)
        {
            String currentArgument = items[i];
            if (currentArgument.equals("("))
            {
                relativePriority += 5;
            }
            else if (currentArgument.equals(")"))
            {
                relativePriority -= 5;
            }
            else
            {
                OperationContainer currentOp = getOperation(currentArgument);
                if (currentOp == null)
                {
                    result += currentArgument + " ";
                }
                else
                {
                    int test = -1;
                    if (stack.size() > 0)
                    {
                        test = stackOrder.get(stack.size()-1);
                    }
                    while (currentOp.ORDER + relativePriority < test)
                    {
                        result += stack.get(stack.size()-1) + " ";
                        stack.remove(stack.size()-1);
                        stackOrder.remove(stackOrder.size()-1);
                        test = -1;
                        if (stack.size() > 0)
                        {
                            test = stackOrder.get(stack.size()-1);
                        }
                    }
                    stack.add(currentArgument);
                    stackOrder.add(currentOp.ORDER+relativePriority);
                }
            }
        }
        while (stack.size() > 0)
        {
            result += stack.get(stack.size()-1) + " ";
            stack.remove(stack.size()-1);
            stackOrder.remove(stackOrder.size()-1);
        }
        return result;
    }
    /**
     * Creates a expression data structure from a string array of different components from a starting position in that array
     * 
     * Use recursively to find the entire expression in the list
     * 
     * @param items The items specifying the data structure
     * @param position The starting position of creating the structure 
     * @param notation The type of notation used in the string array e.g prefix postfix etc.
     * @return Returns a term object that reflects the expression written in some notation
     */
    private static ArgumentObject generateTerm(String[] items, int position, NotationType notation)
    {
        String intialArgument = items[position];

        Operation.Operator operation = null;
        boolean variableFound = false;
        Complex value = null;
        boolean doesOperationUseOneArgument = false;
        Term prespecifiedSecondTerm = null; //Used to fill missing terms e.g. ln x is a valid expression but instead should be save as LOG E x
        if (notation == NotationType.INFIX)
        {
             /*
            -------------------------------------
            If in INFIX Notation (The hardest one to code :( )
            ------------------------------------
            */
            throw new UnsupportedOperationException("Cannot load infix directly, consider converting to postifx first");
        }
        else
        { 
            /*
            -------------------------------------
            If in PREFIX or POSTFIX notation
            ------------------------------------
            */
            OperationContainer currentOp = getOperation(intialArgument);

            if (currentOp == null)
            { //If I'm not an operation
                value = getValue(intialArgument);
                variableFound = value == null;
            }
            else
            {
                operation = currentOp.OPERATION;
                doesOperationUseOneArgument = currentOp.HASONLYONEARGUMENT; //True for operations like sin, cos, arg and ln
                prespecifiedSecondTerm = currentOp.PRESPECIFIEDSECONDTERM; //If the operation would usually take two arguments/terms but in this special case only take one e.g. ln
            }
            if (operation != null)
            {
                ArgumentObject argument1;
                ArgumentObject argument2; 
                if (notation == NotationType.PREFIX)
                { //Using prefix notation
                    if (doesOperationUseOneArgument)
                    {
                        argument1 = generateTerm(items, position+1, notation);
                        return new ArgumentObject (new Operation(argument1.RESULTANTTERM, operation, prespecifiedSecondTerm), argument1.ENDINGPOSITION);
                    }
                    else
                    {
                        argument1 = generateTerm(items, position+1, notation);
                        argument2 = generateTerm(items, argument1.ENDINGPOSITION+1, notation);
                        return new ArgumentObject (new Operation(argument1.RESULTANTTERM, operation, argument2.RESULTANTTERM), argument2.ENDINGPOSITION);
                    }
                }
                else
                { //Using postfix notation
                    if (doesOperationUseOneArgument)
                    {
                        argument1 = generateTerm(items, position-1, notation);
                        return new ArgumentObject (new Operation(argument1.RESULTANTTERM, operation, prespecifiedSecondTerm), argument1.ENDINGPOSITION);
                    }
                    else
                    {
                        argument2 = generateTerm(items, position-1, notation);
                        argument1 = generateTerm(items, argument2.ENDINGPOSITION-1, notation);
                        return new ArgumentObject (new Operation(argument1.RESULTANTTERM, operation, argument2.RESULTANTTERM), argument1.ENDINGPOSITION);
                    }
                }


            }
            else if (variableFound)
            { //Add variable
                 return new ArgumentObject (new Variable(items[position]), position);
            }
            else
            { //Add just a value
                return new ArgumentObject (new Value(value), position);
            }
        }
    }
    
    
    public static class OperationContainer
    {
        public final Operation.Operator OPERATION;
        public final boolean HASONLYONEARGUMENT;
        public final Term PRESPECIFIEDSECONDTERM;
        public final int ORDER;
        
        public OperationContainer(Operation.Operator operation, boolean hasOnlyOneArgument, Term prespecifiedSecondTerm, int order)
        {
            this.OPERATION = operation;
            this.HASONLYONEARGUMENT = hasOnlyOneArgument;
            this.PRESPECIFIEDSECONDTERM = prespecifiedSecondTerm;
            this.ORDER = order;
        }
    }
    /**
     * Gets the current operation specified by the argument given used internally
     * 
     * KEYWORDS

        ADDITION
        +, add, addition

        SUBTRACTION
        -, sub, subtract, subtraction

        MULTIPLICATION
        *, mult multiply, multiplication

        DIVISION
        /, div, divide, division

        MODULO
        %, mod, modulo, remainder

        POWER
        ^, pow, power

        CHOOSE
        c, choose, comb, combination

        SINE
        sin, sine

        COSINE
        cos, cosine

        TANGENT
        tan, tangent

        INVERSE SINE
        asin, asine, arcsin, arcsine, sin-1, sine-1

        INVERSE COSINE
        acos, acosine, arccos, arccosine, cos-1, cosine-1

        INVERSE TANGENT
        atan, atangent, arctan, arctangent, tan-1, tangent-1

        LOGARITHM
        log, logarithm

        NATURAL LOGARITHM
        ln, loge, naturallog

        LOGARITHM BASE 10
        log10, logarithm10

        FACTORIAL
        !, fact, factorial

        ABSOLUTE VALUE
        abs, absolute, modulus

        COMPLEX ARGUMENT
        arg, argument

        REAL COMPONENT
        real, realcomp

        IMAGINARY COMPONENT
        img, imaginary, imaginarycomp

     * 
     * @param argument
     * @return 
     */
    private static OperationContainer getOperation(String argument)
    {
        Operation.Operator operation = null;
        boolean hasOnlyOneArgument = false;
        Term prespecifiedSecondTerm = null;
        int order; //Means done first BIDMAS
        switch (argument.toLowerCase())
        { //Test different cases
            case "+": //Add
            case "add":
            case "addition":
                operation = Operation.Operator.ADD;
                order = 1;
                break;
            case "-": //Subtract
            case "sub":
            case "subtract":
            case "subtraction":
                operation = Operation.Operator.SUB;
                order = 1;
                break;
            case "*": //Multiply
            case "mult":
            case "multiply":
            case "multiplication":
                operation = Operation.Operator.MULT;
                order = 2;
                break;
            case "/": //Divide
            case "div":
            case "divide":
            case "division":
                operation = Operation.Operator.DIV;
                order = 2;
                break;
            case "%": //MOD
            case "mod": 
            case "modulo":
            case "remainder":
                operation = Operation.Operator.MOD;
                order = 2;
                break;
            case "^": //Power
            case "pow":
            case "power":
            case "tothepowerof":
            case "raisedtothepowerof":
                operation = Operation.Operator.POW;
                order = 4;
                break;
            case "ncr": //nCr
            case "choose":
            case "comb":
            case "combination":
                operation = Operation.Operator.COMB;
                order = 3;
                break;
            case "sin": //Sine
            case "sine":
                operation = Operation.Operator.SIN;
                hasOnlyOneArgument = true; //Sin only uses one argument
                order = 3;
                break;
            case "cos": //Cosine
            case "cosine":
                operation = Operation.Operator.COS;
                hasOnlyOneArgument = true; //Cosine only uses one argument
                order = 3;
                break;
            case "tan": //Tangent
            case "tangent":
                operation = Operation.Operator.TAN;
                hasOnlyOneArgument = true; //Tangent only uses one argument
                order = 3;
                break;
            case "cosec": //Cosecant
            case "cosecant":
                operation = Operation.Operator.COSEC;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "sec": //Secant
            case "secant":
                operation = Operation.Operator.SEC;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "cot": //Cotangent
            case "cotangent":
                operation = Operation.Operator.COT;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "asin": //Arc Sine
            case "asine":
            case "arcsin":
            case "arcsine":
            case "sin-1":
            case "sine-1":
                operation = Operation.Operator.ASIN;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "acos": //Arc Cosine
            case "acosine":
            case "arccos":
            case "arccosine":
            case "cos-1":
            case "cosine-1":
                operation = Operation.Operator.ACOS;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "atan": //Arc Cosine
            case "atangent":
            case "arctan":
            case "arctangent":
            case "tan-1":
            case "tangent-1":
                operation = Operation.Operator.ATAN;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "sinh": //sinh
            case "sineh":
            case "sinhyperbolic":
            case "sinehyperbolic":
            case "hyperbolicsin":
            case "hyperbolicsine":
                operation = Operation.Operator.SINH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "cosh": //Cosh
            case "cosineh":
            case "coshyperbolic":
            case "cosinehyperbolic":
            case "hyperboliccos":
            case "hyperboliccosine":
                operation = Operation.Operator.COSH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "tanh": //Tanh
            case "tangenth":
            case "tanhyperbolic":
            case "tangenthyperbolic":
            case "hyperbolictan":
            case "hyperbolictangent":
                operation = Operation.Operator.TANH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "cosech": //cosech
            case "cosecanth":
            case "cosechyperbolic":
            case "cosecanthyperbolic":
            case "hyperboliccosec":
            case "hyperboliccosecant":
                operation = Operation.Operator.COSECH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "sech": //sech
            case "secanth":
            case "sechyperbolic":
            case "secanthyperbolic":
            case "hyperbolicsec":
            case "hyperbolicsecant":
                operation = Operation.Operator.SECH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "coth": //coth
            case "cotangenth":
            case "cothyperbolic":
            case "cotangenthyperbolic":
            case "hyperboliccot":
            case "hyperboliccotangent":
                operation = Operation.Operator.COTH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "asinh": //inverse sinh
            case "asineh":
            case "asinhyperbolic":
            case "asinehyperbolic":
            case "hyperbolicasin":
            case "hyperbolicasine":
            case "arcsinh":
            case "arcsineh":
            case "arcsinhyperbolic":
            case "arcsinehyperbolic":
            case "hyperbolicarcsin":
            case "hyperbolicarcsine":
            case "arsinh":
            case "arsineh":
            case "arsinhyperbolic":
            case "arsinehyperbolic":
            case "hyperbolicarsin":
            case "hyperbolicarsine":
            case "sinh-1":
            case "sineh-1":
            case "sinhyperbolic-1":
            case "sinehyperbolic-1":
            case "hyperbolicsin-1":
            case "hyperbolicsine-1":
                operation = Operation.Operator.ASINH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "arccosh": //inverse Cosh
            case "arccosineh":
            case "arccoshyperbolic":
            case "arccosinehyperbolic":
            case "archyperboliccos":
            case "hyperbolicarccosine":
            case "arcosh":
            case "arcosineh":
            case "arcoshyperbolic":
            case "arcosinehyperbolic":
            case "arhyperboliccos":
            case "hyperbolicarcosine":
            case "acosh":
            case "acosineh":
            case "acoshyperbolic":
            case "acosinehyperbolic":
            case "ahyperboliccos":
            case "hyperbolicacosine":
            case "cosh-1":
            case "cosineh-1":
            case "coshyperbolic-1":
            case "cosinehyperbolic-1":
            case "hyperboliccos-1":
            case "hyperboliccosine-1":
                operation = Operation.Operator.ACOSH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "arctanh": //inverse Tanh
            case "arctangenth":
            case "arctanhyperbolic":
            case "arctangenthyperbolic":
            case "hyperbolicarctan":
            case "hyperbolicarctangent":
            case "artanh":
            case "artangenth":
            case "artanhyperbolic":
            case "artangenthyperbolic":
            case "hyperbolicartan":
            case "hyperbolicartangent":
            case "atanh":
            case "atangenth":
            case "atanhyperbolic":
            case "atangenthyperbolic":
            case "hyperbolicatan":
            case "hyperbolicatangent":
            case "tanh-1":
            case "tangenth-1":
            case "tanhyperbolic-1":
            case "tangenthyperbolic-1":
            case "hyperbolictan-1":
            case "hyperbolictangent-1":
                operation = Operation.Operator.ATANH;
                hasOnlyOneArgument = true; //Only uses one argument
                order = 3;
                break;
            case "log": //Logarithm
            case "lg":
            case "logarithm":
                operation = Operation.Operator.LOG;
                order = 3;
                break;
            case "ln": //Natrual logarithm
            case "loge":
            case "lge":
            case "naturallog":
                operation = Operation.Operator.LOG;
                prespecifiedSecondTerm = new Value(Complex.E()); //Set the base to E
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "log10": //logarithm base 10
            case "lg10": //logarithm base 10
            case "logarithm10": //logarithm base 10
                operation = Operation.Operator.LOG;
                prespecifiedSecondTerm = new Value(10); //Set the base to E
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "logpi": //logarithm base pi
            case "lgpi": //logarithm base pi
            case "logarithmpi": //logarithm base pi
                operation = Operation.Operator.LOG;
                prespecifiedSecondTerm = new Value(Complex.PI()); //Set the base to E
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "!":
            case "fact":
            case "factorial":
                operation = Operation.Operator.FACTORIAL;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "Γ":
            case "gamma":
            case "gammafunction":
                operation = Operation.Operator.GAMMA;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "abs":
            case "absolute":
            case "modulus":
                operation = Operation.Operator.ABS;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "arg":
            case "argument":
                operation = Operation.Operator.ARG;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "real":
            case "realcomp":
                operation = Operation.Operator.REAL;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            case "img":
            case "imaginary":
            case "imaginarycomp":
                operation = Operation.Operator.REAL;
                hasOnlyOneArgument = true;  //Only uses one argument
                order = 3;
                break;
            default: //Not operator e.g value or variable 
                return null;
        }
        return new OperationContainer(operation, hasOnlyOneArgument, prespecifiedSecondTerm, order); //Return operation result
    }
    /**
     * Used for turning values into useable data structures
     * 
     * KEYWORDS
    
        EULER'S NUMBER
        e, eulers, eulersnumber, eulersconstant (With or without apostrophe)

        PI
        pi, π

        GOLDEN RATIO
        phi, φ, goldenratio

        IMAGINARY CONSTANT
        i
     * 
     * @param argument 
     */
    private static Complex getValue(String argument)
    {
        Complex value;
        switch (argument.toLowerCase())
        { //Test different cases
            //Mathematical constants
            case "e": //Eulers constant
            case "eulers":
            case "euler's":
            case "eulersnumber":
            case "euler'snumber":
            case "eulersconstant":
            case "euler'sconstant":
                value = Complex.E();
                break;
            case "pi": //Pi
            case "π": //Pi
                value = Complex.PI();
                break;
            case "phi": //PHI
            case "φ":
            case "goldenratio":
                value = Complex.PHI();
                break;
            case "i": //I
                value = Complex.I();
                break;
            case "inf": //Infinity
            case "infinite":
            case "infinity":
            case "∞":
                value = new Complex(Complex.OtherValues.PLUSINFINITY);
                break;
            default: //Not operator e.g value or variable 
                try
                {
                    switch (argument.toLowerCase().charAt(argument.length()-1))
                    {
                        case 'i':
                            value = new Complex(0, Double.parseDouble(argument.substring(0, argument.length()-1)));
                            break;
                        case '%':
                            value = new Complex(Double.parseDouble(argument.substring(0, argument.length()-1)) / 100.0);
                            break;
                        default:
                            value = new Complex(Double.parseDouble(argument));
                            break;
                    }
                }
                catch (NumberFormatException e)
                {
                    return null;
                }
                break;
        }
        return value;
    }
} 
 