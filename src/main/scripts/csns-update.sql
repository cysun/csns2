update authorities set role = 'ROLE_DEPT' || substring(role from 10)
	where left(role,9) = 'DEPT_ROLE';
