/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.math.stat.univariate.moment;

/**
 * @author Mark Diggory
 */
public class Kurtosis extends FourthMoment {

    private double kurtosis = Double.NaN;

    /**
     * @see org.apache.commons.math.stat.univariate.StorelessUnivariateStatistic#increment(double)
     */
    public double increment(double d) {
        super.increment(d);
        
        double variance = (n <= 1) ? 0.0 : m2 / (double) (n - 1);

        kurtosis =
            (n <= 3 || variance < 10E-20)
                ? 0.0
                : ((double)n * ((double)n + 1) * m4 - 3 * m2 * m2 * (n-1))
                    / ((n-1) * (n-2) * (n-3) * variance * variance);
        
        return kurtosis;
    }
    
    /**
     * @see org.apache.commons.math.stat.univariate.StorelessUnivariateStatistic#getValue()
     */
    public double getValue() {
        return kurtosis;
    }
    
    /**
     * @see org.apache.commons.math.stat.univariate.StorelessUnivariateStatistic#clear()
     */
    public void clear() {
        super.clear();
        kurtosis = Double.NaN;
    }

    /**
        * Returns the kurtosis for this collection of values. Kurtosis is a 
        * measure of the "peakedness" of a distribution.
        * @param values Is a double[] containing the values
        * @param begin processing at this point in the array
        * @param length processing at this point in the array
        * @return the kurtosis of the values or Double.NaN if the array is empty
        */
       public double evaluate(double[] values, int begin, int length) {
           test(values, begin, length);

           // Initialize the kurtosis
           double kurt = Double.NaN;

           // Get the mean and the standard deviation
           double mean = super.evaluate(values, begin, length);

           // Calc the std, this is implemented here instead of using the 
           // standardDeviation method eliminate a duplicate pass to get the mean
           double accum = 0.0;
           double accum2 = 0.0;
           for (int i = begin; i < begin + length; i++) {
               accum += Math.pow((values[i] - mean), 2.0);
               accum2 += (values[i] - mean);
           }
        
           double stdDev =
               Math.sqrt(
                   (accum - (Math.pow(accum2, 2) / ((double) length)))
                       / (double) (length - 1));

           // Sum the ^4 of the distance from the mean divided by the 
           // standard deviation
           double accum3 = 0.0;
           for (int i = begin; i < begin + length; i++) {
               accum3 += Math.pow((values[i] - mean) / stdDev, 4.0);
           }

           // Get N
           double n = length;

           double coefficientOne = (n * (n + 1)) / ((n - 1) * (n - 2) * (n - 3));
           double termTwo = ((3 * Math.pow(n - 1, 2.0)) / ((n - 2) * (n - 3)));
        
           // Calculate kurtosis
           kurt = (coefficientOne * accum3) - termTwo;

           return kurt;
       }
       
}
