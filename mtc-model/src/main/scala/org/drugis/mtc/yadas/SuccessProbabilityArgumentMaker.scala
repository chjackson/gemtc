/*
 * This file is part of drugis.org MTC.
 * MTC is distributed from http://drugis.org/mtc.
 * Copyright (C) 2009-2010 Gert van Valkenhoef.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.drugis.mtc.yadas

import org.drugis.mtc._
import gov.lanl.yadas.ArgumentMaker

/**
 * ArgumentMaker for individual treatment success probabilities within studies.
 * p_i,k = ilogit(theta_i,k) ; theta_i,k = mu_i + delta_i,b(i),k
 */
class SuccessProbabilityArgumentMaker(
		override val model: NetworkModel[DichotomousMeasurement, _],
		override val sIdx: Int,
		override val dIdx: Int,
		override val study: Study[DichotomousMeasurement])
extends ArgumentMaker with ThetaMaker[DichotomousMeasurement] {
	/**
	 * Calculate "the argument": an array of succes-probabilities, one for
	 * each study-arm.
	 * data[sIdx] should contain study baseline means, one per study
	 * data[dIdx] should contain relative effects, one for each non-baseline
	 */
	def getArgument(data: Array[Array[Double]]): Array[Double] = {
		val rval = Array.make(0, 0.0) ++ {
			for {t <- treatments} yield prob(t, data)
		}
		if (rval.length != treatments.size) throw new RuntimeException(rval + " does not match length " + treatments.size + " given data " + data)
		else rval
	}

	private def ilogit(x: Double): Double = 1 / (1 + Math.exp(-x))

	private def prob(t: Treatment, data: Array[Array[Double]])
	: Double = {
		ilogit(theta(t, data))
	}
}
