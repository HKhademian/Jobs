//package ir.hossainco.jobs.data.database
//
//import android.arch.lifecycle.LiveData
//import android.arch.persistence.room.*
//import ir.hossainco.jobs.data.model.Request
//import ir.hossainco.jobs.util.BaseDao
//import ir.hossainco.jobs.util.BaseEntity
//
//@Entity(tableName = "Request")
//class DbRequest(id: Long = 0, worker: Boolean = false, detail: String = "") : BaseEntity(), Request {
//	@PrimaryKey(autoGenerate = true)
//	override var id: Long = id
//		private set
//
//	// if request for work (Job seeker) not for worker (Employer)
//	override var worker: Boolean = worker
//		private set
//
//	// worker|job request details
//	override var detail: String = detail
//		private set
//}
//
//
//@Entity(tableName = "RequestJoinAbility",
//	primaryKeys = ["requestId", "abilityId"],
//	foreignKeys = [
//		ForeignKey(entity = DbRequest::class, parentColumns = ["id"], childColumns = ["requestId"]),
//		ForeignKey(entity = DbAbility::class, parentColumns = ["id"], childColumns = ["abilityId"])
//	])
//class DbRequestJoinAbility(
//	val requestId: Long = 0,
//	val abilityId: Long = 0
//)
//
//
//@Dao
//abstract class RequestDao : BaseDao<DbRequest>() {
//	@Query("SELECT * FROM `Request`")
//	abstract fun list(): LiveData<List<DbRequest>>
//
//	@Query("SELECT * FROM `Ability` INNER JOIN `RequestJoinAbility` ON `Ability`.`id` = `RequestJoinAbility`.`abilityId` WHERE `RequestJoinAbility`.`requestId` =:requestId")
//	abstract fun listRequestAbilities(requestId: Long): List<Request>
//}
