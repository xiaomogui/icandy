package com.sain.icandy.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSportsEvents<M extends BaseSportsEvents<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return get("id");
	}

	public void setGid(java.lang.String gid) {
		set("gid", gid);
	}

	public java.lang.String getGid() {
		return get("gid");
	}

	public void setLeague(java.lang.String league) {
		set("league", league);
	}

	public java.lang.String getLeague() {
		return get("league");
	}

	public void setLeagueTw(java.lang.String leagueTw) {
		set("league_tw", leagueTw);
	}

	public java.lang.String getLeagueTw() {
		return get("league_tw");
	}

	public void setLeagueEn(java.lang.String leagueEn) {
		set("league_en", leagueEn);
	}

	public java.lang.String getLeagueEn() {
		return get("league_en");
	}

	public void setGnumH(java.lang.String gnumH) {
		set("gnum_h", gnumH);
	}

	public java.lang.String getGnumH() {
		return get("gnum_h");
	}

	public void setGnumC(java.lang.String gnumC) {
		set("gnum_c", gnumC);
	}

	public java.lang.String getGnumC() {
		return get("gnum_c");
	}

	public void setTeamH(java.lang.String teamH) {
		set("team_h", teamH);
	}

	public java.lang.String getTeamH() {
		return get("team_h");
	}

	public void setTeamHTw(java.lang.String teamHTw) {
		set("team_h_tw", teamHTw);
	}

	public java.lang.String getTeamHTw() {
		return get("team_h_tw");
	}

	public void setTeamHEn(java.lang.String teamHEn) {
		set("team_h_en", teamHEn);
	}

	public java.lang.String getTeamHEn() {
		return get("team_h_en");
	}

	public void setTeamC(java.lang.String teamC) {
		set("team_c", teamC);
	}

	public java.lang.String getTeamC() {
		return get("team_c");
	}

	public void setTeamCTw(java.lang.String teamCTw) {
		set("team_c_tw", teamCTw);
	}

	public java.lang.String getTeamCTw() {
		return get("team_c_tw");
	}

	public void setTeamCEn(java.lang.String teamCEn) {
		set("team_c_en", teamCEn);
	}

	public java.lang.String getTeamCEn() {
		return get("team_c_en");
	}

	public void setHgid(java.lang.String hgid) {
		set("hgid", hgid);
	}

	public java.lang.String getHgid() {
		return get("hgid");
	}

	public void setPlay(java.lang.String play) {
		set("play", play);
	}

	public java.lang.String getPlay() {
		return get("play");
	}

	public void setRunTime(java.lang.String runTime) {
		set("runTime", runTime);
	}

	public java.lang.String getRunTime() {
		return get("runTime");
	}

	public void setBeginTime(java.util.Date beginTime) {
		set("beginTime", beginTime);
	}

	public java.util.Date getBeginTime() {
		return get("beginTime");
	}

	public void setStatus(java.lang.String status) {
		set("status", status);
	}

	public java.lang.String getStatus() {
		return get("status");
	}

	public void setPayTime(java.util.Date payTime) {
		set("payTime", payTime);
	}

	public java.util.Date getPayTime() {
		return get("payTime");
	}

	public void setBallcode(java.lang.String ballcode) {
		set("ballcode", ballcode);
	}

	public java.lang.String getBallcode() {
		return get("ballcode");
	}

	public void setScoreH(java.lang.String scoreH) {
		set("score_h", scoreH);
	}

	public java.lang.String getScoreH() {
		return get("score_h");
	}

	public void setScoreC(java.lang.String scoreC) {
		set("score_c", scoreC);
	}

	public java.lang.String getScoreC() {
		return get("score_c");
	}

	public void setRedcardH(java.lang.String redcardH) {
		set("redcard_h", redcardH);
	}

	public java.lang.String getRedcardH() {
		return get("redcard_h");
	}

	public void setRedcardC(java.lang.String redcardC) {
		set("redcard_c", redcardC);
	}

	public java.lang.String getRedcardC() {
		return get("redcard_c");
	}

	public void setLastestscoreH(java.lang.String lastestscoreH) {
		set("lastestscore_h", lastestscoreH);
	}

	public java.lang.String getLastestscoreH() {
		return get("lastestscore_h");
	}

	public void setLastestscoreC(java.lang.String lastestscoreC) {
		set("lastestscore_c", lastestscoreC);
	}

	public java.lang.String getLastestscoreC() {
		return get("lastestscore_c");
	}

	public void setScoreRedcardUpdateTime(java.util.Date scoreRedcardUpdateTime) {
		set("score_redcard_update_time", scoreRedcardUpdateTime);
	}

	public java.util.Date getScoreRedcardUpdateTime() {
		return get("score_redcard_update_time");
	}

}
