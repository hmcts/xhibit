-- Only needs to be called in dev and test. In Live there will be no new instances of these disposals when these initial scripts are run

delete from xhb_ref_disposal_line where disposal_code in ('DISOBLG','DISDISC','DISTOT');

commit;
