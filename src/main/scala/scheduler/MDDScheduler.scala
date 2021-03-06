package scheduler

import instance.{Instance, Job}

/** Class to represent MDD (Modified Due Date) schedulers for the SMTWTP
  * problem.
  *
  * @constructor create a new MDD scheduler.
  * @param instance instance to schedule
  *
  * @author Quentin Baert
  */
class MDDScheduler (override val instance: Instance) extends Scheduler(instance) {

  /** @see scheduler.Scheduler.schedule() */
  override def schedule: Instance = {
    def mddScore(actualTime: Int, job: Job): Int =
      math.max(actualTime + job.execTime, job.dueTime)

    def innerSchedule(jobs: List[Job], scheduledJobs: List[Job], actualTime: Int): List[Job] = {
      if (jobs.isEmpty)
        scheduledJobs
      else {
        val orderedJobs = jobs sortWith ((j1, j2) => mddScore(actualTime, j1) < mddScore(actualTime, j2))
        val job = orderedJobs.head

        innerSchedule(orderedJobs.tail, job :: scheduledJobs, actualTime + job.execTime)
      }
    }

    innerSchedule(this.instance, List(), 0).reverse
  }

}
